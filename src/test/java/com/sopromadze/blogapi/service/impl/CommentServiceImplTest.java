package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {


    CommentServiceImpl commentService;


    CommentService commentServiceInterface;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    UserRepository userRepository;

    User user;
    Comment comment;
    Post post;

    @BeforeEach
    void initData() {

        commentService = new CommentServiceImpl(commentRepository, postRepository, userRepository);

        user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Gutierrez")
                .username("Juanguti")
                .password("lacontrasenadeljuan")
                .email("juanguti@gmail.com")
                .comments(new ArrayList<>())
                .build();


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

    //Funcionamiento correcto de getComment
    //Devolucion de excepcion ResourceNotFound
    //Devolucion de excepcion BlogapiException
    @Test
    public void whenGetCommentFindPostAndComment_success() {


        Mockito.when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        assertEquals(comment, commentService.getComment(post.getId(), comment.getId()));

    }

}