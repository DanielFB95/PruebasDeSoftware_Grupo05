package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    AlbumService albumService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    User user;
    Album album;
    List<Album> albums;

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