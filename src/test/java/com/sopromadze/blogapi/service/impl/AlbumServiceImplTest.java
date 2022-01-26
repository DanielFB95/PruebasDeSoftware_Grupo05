package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Album;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    Album album;
    AlbumResponse albumResponse;
    AlbumRequest albumRequest;
    User user;
    UserPrincipal userPrincipal;

    @BeforeEach
    void initData() {

        user = new User();
        user.setUsername("Pablo");
        user.setFirstName("Pablo");
        user.setLastName("Segura");
        user.setCreatedAt(Instant.now());

        album = new Album();

        album.setTitle("Album 1º");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        albumResponse = new AlbumResponse();

        albumResponse.setTitle("Album 2º");
        albumResponse.setCreatedAt(Instant.now());
        albumResponse.setUpdatedAt(Instant.now());

        albumRequest = new AlbumRequest();

        albumRequest.setTitle("Album 3º");
        albumRequest.setCreatedAt(Instant.now());
        albumRequest.setUpdatedAt(Instant.now());

        userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());



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

    }

    @Test
    void whenAddAlbum_success(){

        modelMapper.map(albumRequest, album);
        when(albumService.addAlbum(albumRequest,userPrincipal)).thenReturn(album);
        assertEquals(album, albumService.addAlbum(albumRequest,userPrincipal));
    }

}
