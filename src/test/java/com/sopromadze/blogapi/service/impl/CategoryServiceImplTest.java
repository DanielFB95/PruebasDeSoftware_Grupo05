package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.mockito.Mockito.*;

import org.springframework.data.domain.Pageable;
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
    Company company;
    User user;
    User userAdmin;
    UserPrincipal userPrincipal;
    UserPrincipal adminPrincipal;
    Post post;
    List<Post> nuevaListaPosts;
    Category categoriaModificada;
    UserPrincipal userNotOwnerPrincipal;
    Pageable pageable;
    Page<Category> pageContentCategory;
    PagedResponse<Category> pagedResponse;
    List<Category>listaCategorias;
    ApiResponse apiResponse;
    ResponseEntity<ApiResponse> apiResponseResponseEntity;

    @BeforeEach
    void init(){

        post = Post.builder()
                .id(1L)
                .title("post")
                .body("cuerpo del post")
                .user(user)
                .category(category)
                .comments(new ArrayList<>())
                .tags(new ArrayList<>())
                .build();

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

        adminPrincipal = UserPrincipal.builder()
                .id(10L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())))
                .build();

        category.setCreatedBy(1L);

        categoryResponseEntityOK = ResponseEntity.ok().body(category);
        categoryResponseEntityCREATED = ResponseEntity.status(HttpStatus.CREATED).body(category);

        categoriaModificada = Category.builder()
                .name("Nueva Categoria")
                .posts(nuevaListaPosts)
                .build();

        nuevaListaPosts = new ArrayList<>();

        userNotOwnerPrincipal = UserPrincipal.builder()
                .id(100L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

        listaCategorias = new ArrayList<>();
        listaCategorias.add(category);

        pageable = PageRequest.of(1,1);
        pageContentCategory = new PageImpl<>(Arrays.asList(category));
        pagedResponse = new PagedResponse<Category>(listaCategorias,pageContentCategory.getNumber()
                ,pageContentCategory.getSize(),pageContentCategory.getTotalElements()
                ,pageContentCategory.getTotalPages(),pageContentCategory.isLast());

        apiResponse = ApiResponse.builder()
                .success(true)
                .message("You successfully deleted category")
                .build();

        apiResponseResponseEntity = ResponseEntity.ok().body(apiResponse);

    }

    @Test
    @DisplayName("geAllCategories funciona correctamente")
    void getAllCategories_success(){

    when(categoryRepository.findAll(any(Pageable.class))).thenReturn(pageContentCategory);
    assertEquals(pagedResponse,categoryServiceImpl.getAllCategories(1,1));

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

    @Test
    @DisplayName("updateCategory funciona correctamente con el usuario que la creó")
    void updateCategory_succesUserOwner(){

        nuevaListaPosts.add(post);
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        category.setName(categoriaModificada.getName());
        category.setPosts(categoriaModificada.getPosts());
        when(categoryRepository.save(any())).thenReturn(category);
        assertEquals(categoryResponseEntityOK,categoryServiceImpl.updateCategory(1L, category,userPrincipal));
    }

    @Test
    @DisplayName("updateCategory funciona correctamente con un administrador")
    void updateCategory_succesUserAdmin(){

        nuevaListaPosts.add(post);
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        category.setName(categoriaModificada.getName());
        category.setPosts(categoriaModificada.getPosts());
        when(categoryRepository.save(any())).thenReturn(category);
        assertEquals(categoryResponseEntityOK,categoryServiceImpl.updateCategory(1L, category,adminPrincipal));
    }

    @Test
    @DisplayName("updateCategory Unauthorized Exception funciona correctamente")
    void updateCategory_UnauthorizedException(){

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        assertThrows(UnauthorizedException.class, () -> categoryServiceImpl.updateCategory(1L,category, userNotOwnerPrincipal));
    }

    @Test
    @DisplayName("deleteCategory funciona correctamente con un usuario creador de la categoria")
    void deleteCategory_successUserOwner(){

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(any(Long.class));
        assertEquals(apiResponseResponseEntity,categoryServiceImpl.deleteCategory(1L, userPrincipal));
    }

    @Test
    @DisplayName("deleteCategory funciona correctamente con un usuario administrador")
    void deleteCategory_successAdmin(){

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(any(Long.class));
        assertEquals(apiResponseResponseEntity,categoryServiceImpl.deleteCategory(1L, adminPrincipal));
    }

    @Test
    @DisplayName("deleteCategory Unauthorized exception")
    void deleteCategory_Unauthorized(){

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        assertThrows(UnauthorizedException.class, () -> categoryServiceImpl.deleteCategory(1L,userNotOwnerPrincipal));
    }
}