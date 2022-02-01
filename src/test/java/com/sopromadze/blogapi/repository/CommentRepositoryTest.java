package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    User user;
    Comment comment;
    Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("user2")
                .lastName("user2")
                .username("useruser")
                .password("contrasena")
                .email("user@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        user.getRoles().add(new Role(RoleName.ROLE_USER));

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        comment = Comment.builder()
                .body("Este post ha cambiado mi vida por completo")
                .name(user.getFirstName())
                .user(user)
                .email(user.getEmail())
                .build();
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());

        post = Post.builder()
                .title("Mi viaje a las Bahamas")
                .body("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum")
                .comments(new ArrayList<>())
                .build();
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());


        post.getComments().add(comment);
        user.getComments().add(comment);
        comment.setPost(post);



    }


    @Test
    @DisplayName("Encontrar una lista paginada de comentarios en un post")
    void findByPostId_success() {
        testEntityManager.persist(user);
        testEntityManager.persist(post);
        testEntityManager.persist(comment);
        testEntityManager.flush();

        Pageable pageable = PageRequest.of(0, 1);


        assertEquals(1L, commentRepository.findByPostId(comment.getId(), pageable).getTotalElements());

    }
}