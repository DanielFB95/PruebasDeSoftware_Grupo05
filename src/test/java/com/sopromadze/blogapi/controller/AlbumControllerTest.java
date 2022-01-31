package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.PhotoService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class AlbumControllerTest {

    @MockBean
    AlbumService albumService;

    @MockBean
    PhotoService photoService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;



    AlbumRequest albumRequest;


    @BeforeEach
    void initData(){

        albumRequest = new AlbumRequest();

        albumRequest.setTitle("Album 3º");
        albumRequest.setCreatedAt(Instant.now());
        albumRequest.setUpdatedAt(Instant.now());

    }

    @Test
    void whenGetAlbums_success() throws Exception{

        mockMvc.perform(get("/api/albums")
                .contentType("application/json")
                        .param("page","1")
                        .param("size","1"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void whenAddAlbum_success() throws Exception{

        mockMvc.perform(post("/api/albums")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(albumRequest)))
                .andExpect(status().isOk());

    }

    @Test
    void whenGetAlbum_success() throws Exception{

        mockMvc.perform(get("/api/albums/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER","ROLE_ADMIN"})
    void whenUpdateAlbum_success() throws Exception{

        mockMvc.perform(put("/api/albums/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(albumRequest)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER","ROLE_ADMIN"})
    void whenDeleteAlbum_success() throws Exception{

        mockMvc.perform(delete("/api/albums/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(albumRequest)))
                .andExpect(status().isNoContent());

    }

    @Test
    void whenGetAllPhotosByAlbum_success() throws Exception{

        mockMvc.perform(get("/api/albums/{id}/photos",1L)
                        .param("page","1")
                        .param("size","1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }


}