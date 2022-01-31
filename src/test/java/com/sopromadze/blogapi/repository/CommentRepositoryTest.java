package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
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

        post = Post.builder()
                .title("Mi viaje a las Bahamas")
                .body("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum")
                .comments(new ArrayList<>())
                .build();



        post.getComments().add(comment);
        user.getComments().add(comment);
        comment.setPost(post);

        testEntityManager.persist(post);
        testEntityManager.persist(user);
        testEntityManager.persist(comment);

    }

    //TODO: Solucionar esta prueba
    @Test
    void findByPostId_success() {


        Page<Comment> result = new PageImpl<>(Arrays.asList(comment));

        Pageable pageable = PageRequest.of(1, 1);

        assertEquals(result, commentRepository.findByPostId(comment.getId(), pageable));

    }
}