package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    @InjectMocks
    AlbumServiceImpl albumService;

    @Mock
     AlbumRepository albumRepository;

    @Mock
     ModelMapper modelMapper;

    @Mock
    UserRepository userRepository;


    private static final String ALBUM_STR = "album";
    private static final String ID_STR = "id";


    Album album;
    AlbumResponse albumResponse;
    AlbumRequest albumRequest;
    User user;
    User admin;
    User userRole;
    UserPrincipal userPrincipal, userPrincipalUser, anotherUserPrincipal;
    ResponseEntity<AlbumResponse> albumResponseResponseEntity;
    ResponseEntity<Album> albumResponseEntity;



    @BeforeEach
    void initData() {

        user = new User();
        user.setId(3L);
        user.setUsername("Pablo");
        user.setFirstName("Pablo");
        user.setLastName("Segura");
        user.setCreatedAt(Instant.now());

        album = new Album();

        album.setTitle("Album 1º");
        album.setId(2L);
        album.setUser(user);
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        albumResponse = new AlbumResponse();

        albumResponse.setTitle("Album 2º");
        albumResponse.setId(7L);
        albumResponse.setUser(user);
        albumResponse.setCreatedAt(Instant.now());
        albumResponse.setUpdatedAt(Instant.now());

        albumRequest = new AlbumRequest();

        albumRequest.setTitle("Album 3º");
        albumRequest.setCreatedAt(Instant.now());
        albumRequest.setUpdatedAt(Instant.now());

        userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());

        admin = User.builder()
                .id(3L)
                .firstName("Pepe")
                .lastName("Fernandez")
                .username("PepeAdmin")
                .password("1234al")
                .email("admin@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        admin.getRoles().add(new Role(RoleName.ROLE_ADMIN));

        userRole = User.builder()
                .id(10L)
                .firstName("Pepa")
                .lastName("Fernandez")
                .username("PepaUser")
                .password("1234al")
                .email("user@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        userRole.getRoles().add(new Role(RoleName.ROLE_USER));
        userPrincipalUser = new UserPrincipal(userRole.getId(), userRole.getFirstName(), userRole.getLastName(), userRole.getUsername(), userRole.getEmail(), userRole.getPassword(),Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));
        anotherUserPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());
        }

        @Test
        void whenGetAlbum_success(){

        when(albumRepository.findById(album.getId())).thenReturn(Optional.of(album));

        albumResponseEntity = ResponseEntity.ok(album);
        assertEquals(albumResponseEntity, albumService.getAlbum(album.getId()));


        }

        @Test
        void whenGetAlbumNotFound_throwNotFoundException(){

            Long idNull = 0L;

            when(albumRepository.findById(idNull)).thenThrow(new ResourceNotFoundException(ALBUM_STR,ID_STR, idNull));
            assertThrows(ResourceNotFoundException.class, () -> albumService.getAlbum(idNull));

            //System.out.println( assertThrows(ResourceNotFoundException.class, () -> albumService.getAlbum(idNull)));

        }

    @Test
    void whenGetAllAlbums_success(){

        List<AlbumResponse> data2 = Arrays.asList(albumResponse);
        Page<Album> data = new PageImpl<>(Arrays.asList(album));

        PagedResponse<AlbumResponse> result = new PagedResponse<>();
        result.setContent(data2);
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        List<Album> albums = new ArrayList<>();
        albums.add(album);
        AlbumResponse [] albumResponses ={albumResponse};

        when(albumRepository.findAll(any(Pageable.class))).thenReturn(data);
        when(modelMapper.map(data.getContent(), AlbumResponse[].class)).thenReturn(albumResponses);

        assertEquals(result, albumService.getAllAlbums(1,1));

    }

    @Test
    void whenDeleteAlbum_success(){

        AlbumServiceImpl albumServiceImpl = mock(AlbumServiceImpl.class);

        albumRepository.save(album);

        doNothing().when(albumServiceImpl).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));
        albumServiceImpl.deleteAlbum(1L,userPrincipal);

        verify(albumServiceImpl, times(1)).deleteAlbum(1L, userPrincipal);

        System.out.println(albumRepository.findById(1L));

    }

    @Test
    void whenAddAlbum_success(){

        modelMapper.map(albumRequest, album);
        when(albumService.addAlbum(albumRequest,userPrincipal)).thenReturn(album);
        assertEquals(album, albumService.addAlbum(albumRequest,userPrincipal));
    }

    @Test
    void whenUpdateAlbum_success() {


        AlbumRequest newAlbum = new AlbumRequest();

        newAlbum.setTitle("Album 4º");
        newAlbum.setId(2L);
        newAlbum.setUser(user);
        newAlbum.setCreatedAt(Instant.now());
        newAlbum.setUpdatedAt(Instant.now());



        when(albumRepository.findById(any(Long.class))).thenReturn(Optional.of(album));
        when(userRepository.getUser(userPrincipal)).thenReturn(user);

        album.setTitle(newAlbum.getTitle());
        when(albumRepository.save(album)).thenReturn(album);



        albumResponse = new AlbumResponse();
        albumResponse.setTitle(album.getTitle());
        albumResponse.setId(album.getId());
        albumResponse.setUser(user);

        albumResponseResponseEntity = ResponseEntity.ok(albumResponse);


        assertEquals(HttpStatus.OK, albumService.updateAlbum(album.getId(), newAlbum,userPrincipal).getStatusCode());
        //assertEquals(albumResponse.getTitle(), albumService.updateAlbum(2L, newAlbum,userPrincipal).getBody().getTitle());
    }

    /*@Test
    void updateAlbumUnauthorized_blogApiException() {


        AlbumRequest newAlbum = new AlbumRequest();

        newAlbum.setTitle("Album 4º");
        newAlbum.setId(2L);
        newAlbum.setUser(userRole);
        newAlbum.setCreatedAt(Instant.now());
        newAlbum.setUpdatedAt(Instant.now());

        when(albumRepository.findById(any(Long.class))).thenReturn(Optional.of(album));

        newAlbum.setTitle(album.getTitle());

        assertThrows(BlogapiException.class, () -> albumService.updateAlbum(album.getId(),newAlbum, anotherUserPrincipal));

    }*/


}
