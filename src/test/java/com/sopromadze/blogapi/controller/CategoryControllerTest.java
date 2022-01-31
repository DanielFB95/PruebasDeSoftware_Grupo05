package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configuration.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.CategoryService;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes ={SpringSecurityTestWebConfig.class} )
@AutoConfigureMockMvc
@Log
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Category category;
    Page<Category> resultado;
    PagedResponse<Category> categoryPagedResponse;
    List<Category> listaCategorias;

    @BeforeEach
    void setUp() {

        category = Category.builder()
                .id(1L)
                .name("categoria")
                .posts(new ArrayList<>())
                .build();

        listaCategorias.add(category);

        resultado = new PageImpl<>(Arrays.asList(category));
        categoryPagedResponse = new PagedResponse<>();
        categoryPagedResponse.setContent(resultado.getContent());
        categoryPagedResponse.setTotalPages(1);
        categoryPagedResponse.setTotalElements(1);
        categoryPagedResponse.setSize(1);
        categoryPagedResponse.setLast(true);

    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET  all Categories funciona correctamente")

    void getCategoryController_success() throws Exception{

        when(categoryService.getAllCategories(any(Integer.class),any(Integer.class))).thenReturn(categoryPagedResponse);

        MvcResult result = mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(listaCategorias)))
                .andReturn();
    }
}