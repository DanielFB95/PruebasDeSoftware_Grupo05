package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserIdentityAvailability;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import lombok.extern.java.Log;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes ={SpringSecurityTestConfig.class} )
@AutoConfigureMockMvc
@Log
class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AlbumService albumService;

    User user;
    Album album;
    List<Album> albums;

    UserPrincipal userPrincipal;
    UserSummary userSummary;
    ResponseEntity<UserSummary> userSummaryResponseEntity;
    UserIdentityAvailability userIdentityAvailability;
    ResponseEntity<UserIdentityAvailability> userIdentityAvailabilityResponseEntity;

    @BeforeEach
    void initData(){

        album = new Album();
        album.setTitle("Album 1º");
        album.setId(2L);
        album.setUser(user);
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        albums = new ArrayList<>();
        albums.add(album);

        user = User.builder()
                .firstName("Juan")
                .lastName("Gutierrez")
                .username("Juanguti")
                .password("lacontrasenadeljuan")
                .email("juanguti@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .albums(albums)
                .build();

        user.getRoles().add(new Role(RoleName.ROLE_USER));

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

            userPrincipal = UserPrincipal.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            userSummary = UserSummary.builder()
                    .id(userPrincipal.getId())
                    .username(userPrincipal.getUsername())
                    .firstName(userPrincipal.getFirstName())
                    .lastName(userPrincipal.getLastName())
                    .build();

            userSummaryResponseEntity = ResponseEntity.ok().body(userSummary);

            userIdentityAvailability = UserIdentityAvailability.builder()
                    .available(true)
                    .build();

            userIdentityAvailabilityResponseEntity = ResponseEntity.ok().body(userIdentityAvailability);







        }

    @Test
    @WithUserDetails("user")
    @DisplayName("GET traer un usuario con un usuario valido")
    void getCurrentUser_successUserOwner() throws Exception{

        when(userService.getCurrentUser(ArgumentMatchers.any())).thenReturn(userSummary);
        mockMvc.perform(get("/api/users/me").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userPrincipal)))
                .andExpect(jsonPath("$.id", Matchers.equalTo(user.getId())))
                .andExpect(jsonPath("$.username", Matchers.equalTo(user.getUsername())));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET traer un usuario con un usuario no válido")
    void getCurrentUser_Unauthorized() throws Exception{

        when(userService.getCurrentUser(ArgumentMatchers.any())).thenReturn(userSummary);
        mockMvc.perform(get("/api/users/me").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userPrincipal)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET confirmar si el nombre de usuario está disponible funciona correctamente")
    void checkUsernameAvailability() throws Exception {

        when(userService.checkUsernameAvailability(ArgumentMatchers.any())).thenReturn(userIdentityAvailability);
        mockMvc.perform(get("/api/users/checkUsernameAvailability").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPrincipal)).param("username", userPrincipal.getUsername()))
                .andExpect(jsonPath("$.available", Matchers.equalTo(true)));
    }

    @Test
    void whenGetUserAlbums_success() throws Exception{

        mockMvc.perform(get("/api/users/{username}/albums",user.getUsername())
                        .param("page","1")
                        .param("size","1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void whenAddUser_success() throws Exception{

        mockMvc.perform(post("/api/users")
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void whenGiveAdmin_success() throws Exception{

        ApiResponse response = new ApiResponse(Boolean.TRUE, "Permisos otorgados");

        mockMvc.perform(put("/api/users/{username}/giveAdmin",user.getUsername())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());


    }

}