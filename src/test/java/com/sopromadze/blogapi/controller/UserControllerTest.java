package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import lombok.extern.java.Log;
import org.apache.catalina.connector.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;


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

    User user;
    UserPrincipal userPrincipal;
    UserSummary userSummary;
    ResponseEntity<UserSummary> userSummaryResponseEntity;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .username("DanielFB")
                .firstName("Daniel")
                .lastName("Fernández")
                .roles(new ArrayList<>())
                .build();

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
    }

    @Test
    @WithUserDetails("user")
    @DisplayName("GET traer un usuario con un usuario valido")
    void getCurrentUser_successUserOwner() throws Exception{

        when(userService.getCurrentUser(ArgumentMatchers.any())).thenReturn(userSummary);
        mockMvc.perform(get("/api/users/me").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userPrincipal)))
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.username", Matchers.equalTo("DanielFB")))
                .andReturn();
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET traer un usuario con un usuario no válido")
    void getCurrentUser_Unauthorized() throws Exception{

        when(userService.getCurrentUser(ArgumentMatchers.any())).thenReturn(userSummary);
        mockMvc.perform(get("/api/users/me").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userPrincipal)))
                .andExpect(status().isForbidden());
    }

}