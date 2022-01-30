package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @MockBean
    TestEntityManager testEntityManager;

    @MockBean
    CommentRepository commentRepository;



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
    void findByPostId_success() {

        Page<Comment> result = new PageImpl<>(Arrays.asList(comment));

        Pageable pageable = PageRequest.of(1, 1);

        when(commentRepository.findByPostId( 1L, pageable)).thenReturn(result);

        assertEquals(result, commentRepository.findByPostId(1L, pageable));


    }
}