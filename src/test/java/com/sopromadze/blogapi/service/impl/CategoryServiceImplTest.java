package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    Category category;
    List <Post> postsList;
    ResponseEntity<Category> categoryResponseEntityOK;
    ResponseEntity<Category> categoryResponseEntityCREATED;
    Role roleAdmin;
    Role roleUser;
    List<Role> listaRoles;
    Company company;
    User user;
    User userAdmin;
    UserPrincipal userPrincipal;

    @BeforeEach
    void init(){

        postsList = new ArrayList<>();

        category = Category.builder()
                .id(1L)
                .name("categoria")
                .posts(postsList)
                .build();

        roleAdmin = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        roleUser = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_USER)
                .build();

        company = Company.builder()
                .id(2L)
                .name("Compañia S.L")
                .catchPhrase("frase")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Daniel")
                .lastName("Fernández")
                .username("danielfb")
                .password("12345")
                .email("danielfb@gmail.com")
                .address(null)
                .phone("620189675")
                .todos(new ArrayList<>())
                .albums(new ArrayList<>())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .company(company)
                .roles(Arrays.asList(roleUser))
                .build();

        user.getRoles().add(roleUser);

        userAdmin = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Fernández")
                .username("juan")
                .password("12345")
                .email("juan@gmail.com")
                .address(null)
                .phone("600000000")
                .todos(new ArrayList<>())
                .albums(new ArrayList<>())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .company(company)
                .roles(Arrays.asList(roleAdmin))
                .build();

        userAdmin.getRoles().add(roleAdmin);

        userPrincipal = UserPrincipal.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

        categoryResponseEntityOK = ResponseEntity.ok().body(category);
        categoryResponseEntityCREATED = ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Test
    @DisplayName("getCategory id 1 funciona correctamente")
    void getCategory_success() {

        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        assertEquals(categoryResponseEntityOK , categoryServiceImpl.getCategory(category.getId()));
    }

    @Test
    @DisplayName("getCategory null Resource Not Found Exception")
    void getCategory_Exception(){

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(null));
    }

    @Test
    @DisplayName("addCategory funciona correctamente")
    void addCategory_success(){

        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(categoryResponseEntityCREATED,categoryServiceImpl.addCategory(category,userPrincipal));
    }

    @Test
    @DisplayName("updateCategory encuentra la categoria por id")
    void updateCategory_findById(){


        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        assertEquals(Optional.of(category), categoryRepository.findById(1L));


    }

    @Test
    @DisplayName("updateCategory null Resource Not Found Exception")
    void updateCategory_Exception(){

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.updateCategory(null,category,userPrincipal));
    }
}