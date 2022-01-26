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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private TestEntityManager testEntityManager;

    Category c;
    List <Post> postsList;
    ResponseEntity<Category> categoryResponseEntityOK;
    ResponseEntity<Category> categoryResponseEntityCREATED;
    Role roleAdmin;
    Role roleUser;
    List<Role> listaRoles;
    Company company;
    User user;
    UserPrincipal userPrincipal;

    @BeforeEach
    void init(){

        postsList = new ArrayList<>();

        c = Category.builder()
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

        listaRoles = new ArrayList<>();
        listaRoles.add(roleAdmin);
        listaRoles.add(roleUser);

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
                .website("www.danielfb.com")
                .roles(listaRoles)
                .todos(new ArrayList<>())
                .albums(new ArrayList<>())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .company(company)
                .build();

        userPrincipal = UserPrincipal.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

        categoryResponseEntityOK = ResponseEntity.ok().body(c);
        categoryResponseEntityCREATED = ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @Test
    @DisplayName("getCategory id 1 funciona correctamente")
    void getCategory_success() {

        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(c));
        assertEquals(categoryResponseEntityOK , categoryServiceImpl.getCategory(c.getId()));
    }

    @Test
    @DisplayName("getCategory null Resource Not Found Exception")
    void getCategory_Exception(){

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(null));
    }

    @Test
    @DisplayName("addCategory funciona correctamente")
    void addCategory_success(){

        testEntityManager.persist(c);
        assertEquals(categoryResponseEntityCREATED,categoryServiceImpl.addCategory(c,userPrincipal));
    }
}