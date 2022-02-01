package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.InfoRequest;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;


    User user;
    User admin;
    UserPrincipal userPrincipal;
    Address address;
    static String REQUEST_MAPPING = "/api/users";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Usuario")
                .lastName("Existente")
                .username("unusuario")
                .password("usuariousuario")
                .email("elusuario@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .posts(new ArrayList<>())
                .build();

        address = new Address("Calle Vieja",
                "Vieja Suite",
                "Vieja Ciudad",
                "11111",
                null);

        user.setAddress(address);

        admin = User.builder()
                .id(2L)
                .firstName("Admin")
                .lastName("El admin")
                .username("eladmin")
                .password("adminadmin")
                .email("eladmin@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        user.getRoles().add(new Role(RoleName.ROLE_USER));
        admin.getRoles().add(new Role(RoleName.ROLE_ADMIN));

        userPrincipal = UserPrincipal.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

    }

    @Test
    @DisplayName("Quitar los privilegios de admin a un ADMIN")
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsService")
    void takeAdmin_success() throws Exception {
        ApiResponse response = new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + admin.getUsername());

        mockMvc.perform(put(REQUEST_MAPPING + "/{username}/takeAdmin", admin.getUsername())
                .contentType("application/json").content(objectMapper.writeValueAsString(response))).andExpect(status().isOk());

    }

    @Test
    @DisplayName("Intentar quitar los privilegios de un admin con privilegios de USER / RETURN 401")
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void takeAdmin_forbidden() throws Exception {
        ApiResponse response = new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + admin.getUsername());

        mockMvc.perform(put(REQUEST_MAPPING + "/{username}/takeAdmin", admin.getUsername())
                .contentType("application/json").content(objectMapper.writeValueAsString(response))).andExpect(status().isForbidden());

    }


    @Test
    @DisplayName("PUT / USER Actualizar la direccion")
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void setAddress_success() throws Exception {

        InfoRequest newAddress = InfoRequest.builder()
                .street("Calle Nueva")
                .suite("Suite Nueva")
                .city("Ciudad Nueva")
                .zipcode("23456").build();

        mockMvc.perform(put(REQUEST_MAPPING + "/setOrUpdateInfo")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newAddress)))
                .andExpect(status().isOk());

    }


}