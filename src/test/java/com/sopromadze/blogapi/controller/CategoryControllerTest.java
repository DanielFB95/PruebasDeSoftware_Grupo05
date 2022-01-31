package com.sopromadze.blogapi.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CategoryService;
import lombok.extern.java.Log;
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
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes ={SpringSecurityTestConfig.class} )
@AutoConfigureMockMvc
@Log
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Category category1, category2, newCategory;
    Page<Category> resultado;
    PagedResponse<Category> categoryPagedResponse;
    List<Category> listaCategorias;
    UserPrincipal user;
    ResponseEntity<Category> categoryResponseEntity;
    ApiResponse apiResponse;
    ResponseEntity<ApiResponse> apiResponseResponseEntity;

    @BeforeEach
    void setUp() {

        category1 = Category.builder()
                .id(1L)
                .name("categoria")
                .posts(new ArrayList<>())
                .build();

        category2 = Category.builder()
                .id(2L)
                .name("categoria2")
                .posts(new ArrayList<>())
                .build();

        newCategory = Category.builder()
                .id(3L)
                .name("nuevaCategoria")
                .posts(new ArrayList<>())
                .build();

        listaCategorias = new ArrayList<>();
        listaCategorias.add(category1);
        listaCategorias.add(category2);

        resultado = new PageImpl<>(listaCategorias);
        categoryPagedResponse = new PagedResponse<>();
        categoryPagedResponse.setContent(resultado.getContent());
        categoryPagedResponse.setTotalPages(1);
        categoryPagedResponse.setTotalElements(1);
        categoryPagedResponse.setSize(1);
        categoryPagedResponse.setLast(true);

        user = UserPrincipal.builder()
                .id(1L)
                .build();

        categoryResponseEntity = ResponseEntity.ok().body(category1);

        apiResponse = ApiResponse.builder()
                .message("Categoria eliminada")
                .build();

        apiResponseResponseEntity = ResponseEntity.ok().body(apiResponse);

    }

    @Test
    @DisplayName("GET  traer todas la categorias funciona correctamente")
    void getCategoryController_success() throws Exception{

        when(categoryService.getAllCategories(any(Integer.class),any(Integer.class))).thenReturn(categoryPagedResponse);

        mockMvc.perform(get("/api/categories").contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andReturn();

        //.param("student-id", "1")
    }

    @Test
    @WithMockUser("user")
    @DisplayName("POST añadir una categoria funciona correctamente")
    void addCategory_success() throws Exception{

        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(category1)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("categoria")));
    }

    @Test
    //@WithMockUser("admin")
    @DisplayName("POST añadir categoria con un usuario no válido y da error 401")
    void addCategory_Unauthorized() throws Exception{

        mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(category1)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser("admin")
    @DisplayName("PUT modifica una categoria con un usuario validado funciona correctamente")
    void putCategory_success() throws  Exception{

        when(categoryService.updateCategory(any(Long.class),ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(categoryResponseEntity);
        mockMvc.perform(put("/api/categories/{id}",category1.getId()).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(category1)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("categoria")));
    }

    @Test
    //@WithMockUser("admin")
    @DisplayName("PUT modifica una categoria con un usuario no validado y da error 401")
    void putCategory_Unauthorized() throws  Exception{

        when(categoryService.updateCategory(any(Long.class),ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(categoryResponseEntity);
        mockMvc.perform(put("/api/categories/{id}",category1.getId()).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(category1)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("admin")
    @DisplayName("DELETE elimina una categoria por id con un usuario valido")
    void deleteCategory_success() throws Exception{

        when(categoryService.deleteCategory(any(Long.class),ArgumentMatchers.any())).thenReturn(apiResponseResponseEntity);
        MvcResult requestResult = mockMvc.perform(delete("/api/categories/{id}",category1.getId()))
                .andExpect(status().isOk()).andExpect(status().isOk()).andReturn();

        //ApiResponse result = requestResult.getResponse().getHeader();
        //assertEquals(apiResponse.getMessage(), result);

        //Collection<String> result = requestResult.getResponse().getHeaderNames();
        //LoggerFactory.getLogger(CategoryControllerTest.class).info(requestResult);
        //assertEquals(apiResponse.getMessage(), result);

    }
}