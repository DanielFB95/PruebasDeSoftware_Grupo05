package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.service.AlbumService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;


@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class AlbumControllerTest {

    @MockBean
    AlbumService albumService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void initData(){


    }

    @Test
    void whenGetAlbums_success(){

    }


}