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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Juan")
                .lastName("Gutierrez")
                .username("Juanguti")
                .password("lacontrasenadeljuan")
                .email("juanguti@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        user.getRoles().add(new Role(RoleName.ROLE_USER));

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());




    }


    @Test
    void findByUsernameOrEmail_success() {

        testEntityManager.persist(user);
        assertEquals(Optional.of(user), userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()));


    }
}