package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
     UserRepository userRepository;

    @Autowired
     TestEntityManager testEntityManager;

    User user;

    @BeforeEach
    void init_Data(){

            user = new User();
            user.setUsername("Pablo");
            user.setFirstName("Pablo");
            user.setLastName("Segura");
            user.setEmail("pablo@gmail.com");
            user.setCreatedAt(Instant.now());
            user.setPassword("1234");
            user.setUpdatedAt(Instant.now());

            testEntityManager.persist(user);
        }



    @Test
    void whenFoundUser_existsByEmail_success() {

       //when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertTrue(userRepository.existsByEmail(user.getEmail()));

    }
}