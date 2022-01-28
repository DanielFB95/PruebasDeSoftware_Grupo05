package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    User user;
    Comment comment;
    Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Gutierrez")
                .username("Juanguti")
                .password("lacontrasenadeljuan")
                .email("juanguti@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();


        user.getRoles().add(new Role(RoleName.ROLE_USER));
        comment = Comment.builder()
                .id(1L)
                .body("Este post ha cambiado mi vida por completo")
                .name(user.getFirstName())
                .user(user)
                .email(user.getEmail())
                .build();

        post = Post.builder()
                .id(1L)
                .title("Mi viaje a las Bahamas")
                .body("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum")
                .comments(new ArrayList<>())
                .build();

        post.getComments().add(comment);
        user.getComments().add(comment);
        comment.setPost(post);
    }


    @Test
    void getAllComments_success() throws Exception {

        Page<Comment> page = new PageImpl(Arrays.asList(comment));

        PagedResponse<Comment> result = new PagedResponse();
        result.setContent(page.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);


        mockMvc.perform(get("/api/posts/{postId}/comments", post.getId())
                        .contentType("application/json")
                        .param("page", "1")
                        .param("size", "1")
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk());

    }
}