package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    Category c;
    List <Post> postsList;
    ResponseEntity<Category> categoryResponseEntity;

    @BeforeEach
    void init(){

        postsList = new ArrayList<>();

        c = Category.builder()
                .id(1L)
                .name("categoria")
                .posts(postsList)
                .build();

        Role roleAdmin = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        Role roleUser = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_USER)
                .build();

        List<Role> listaRoles = new ArrayList<>();
        listaRoles.add(roleAdmin);
        listaRoles.add(roleUser);

        UserPrincipal user = UserPrincipal.builder()
                .id(1L)
                .firstName("daniel")
                .lastName("fernández")
                .username("danielfb")
                .email("danielfb@gmail.com")
                .password("12345")
                .authorities(listaRoles)
                .build();

        categoryResponseEntity = ResponseEntity.ok().body(c);
    }

    @Test
    @DisplayName("getCategory con id 1 funciona correctamente")
    void getCategory_success() {

        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(c));
        assertEquals(categoryResponseEntity , categoryServiceImpl.getCategory(c.getId()));
    }

    @Test
    @DisplayName("getCategory null Resource Not Found Exception")
    void getCategory_Exception(){

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(null));
    }

    @Test
    void addCategory_success(){



    }
}