package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    List <Post> posts;
    ResponseEntity<Category> categoryResponseEntity;

    @BeforeEach
    void init(){

        posts = new ArrayList<>();

        c = new Category();
        c.setId(1L);
        c.setName("categoria");
        c.setPosts(posts);

        categoryResponseEntity = ResponseEntity.ok().body(c);
    }

    @Test
    void getCategoryTest() {

        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(c));
        assertEquals(categoryResponseEntity , categoryServiceImpl.getCategory(c.getId()));
    }

    @Test
    void getCategoryTest_Exception(){

        assertThrows(ResourceNotFoundException.class, () -> categoryRepository.findById(anyLong()));
    }
}