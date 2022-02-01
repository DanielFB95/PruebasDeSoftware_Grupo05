package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CommentService;

import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    static String REQUEST_MAPPING = "/api/posts/{postId}/comments";

    User user;
    Comment comment;
    Post post;

    UserPrincipal userPrincipal;

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

       /* userPrincipal = UserPrincipal.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();*/
        userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        post.getComments().add(comment);
        user.getComments().add(comment);
        comment.setPost(post);
    }


    @Test
    @DisplayName("GET / COMMENT devolviendo 200")
    void getAllComments_success() throws Exception {

        Page<Comment> page = new PageImpl(Arrays.asList(comment));

        PagedResponse<Comment> result = new PagedResponse();
        result.setContent(page.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);


        mockMvc.perform(get(REQUEST_MAPPING, post.getId())
                        .contentType("application/json")
                        .param("page", "1")
                        .param("size", "1")
                        .content(objectMapper.writeValueAsString(page)))
                .andExpect(status().isOk());


    }


    @Test
    @DisplayName("POST / COMMENT devolviendo 201")
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void addComment_success() throws Exception {

        CommentRequest newBodyComment = new CommentRequest();
        newBodyComment.setBody("Este es mi nuevo comentario para este post.");

        Comment newComment = Comment.builder().name(user.getUsername())
                .user(user)
                .body(newBodyComment.getBody())
                .email(user.getEmail())
                .post(post).build();

        mockMvc.perform(post(REQUEST_MAPPING, post.getId())
                        .contentType("application/json")
                        .param("commentRequest", "newBodyComment")
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isCreated());


    }

    @Test
    @DisplayName("PUT / COMMENT devolviendo 200")
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void updateComment_success() throws Exception {

        CommentRequest newBodyComment = new CommentRequest();
        newBodyComment.setBody("Nuevo texto para el comentario editado");

        mockMvc.perform(put(REQUEST_MAPPING + "/{id}", post.getId(), comment.getId())
                        .contentType("application/json")
                        .param("commentRequest", "newBodyComment")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("DELETE / COMMENT devolviendo 200")
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void deleteComment_returns200_success() throws Exception {

        ApiResponse response = new ApiResponse(Boolean.TRUE, "You successfully deleted comment");

        lenient().when(commentService.deleteComment(any(Long.class), any(Long.class), any())).thenReturn(response);

        mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", post.getId(), comment.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isOk());

    }


}