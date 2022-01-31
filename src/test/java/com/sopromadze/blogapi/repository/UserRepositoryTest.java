package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

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


        testEntityManager.persist(user);

    }


    @ParameterizedTest
    @MethodSource("getDifferentStrings")
    @DisplayName("Encontrar un usuario por Username o por su email.")
    void findByUsernameOrEmail_success(String username, String email) {


        assertEquals(Optional.of(user), userRepository.findByUsernameOrEmail(username, email));


    }

    private static Stream<Arguments> getDifferentStrings() {

        User mockUser = User.builder()
                .username("Juanguti")
                .email("juanguti@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        return Stream.of(
                Arguments.arguments(mockUser.getUsername(), mockUser.getEmail()), //Pasando username y email
                Arguments.arguments(mockUser.getUsername(), null), //Pasando username, pero no email
                Arguments.arguments(null, mockUser.getEmail())//Pasando email, pero no username
        );
    }

    @Test
    @DisplayName("Encontrar a un usuario buscado por su Username.")
    void getUserByName_success(){

        assertTrue(userRepository.getUserByName(user.getUsername()).equals(user));

    }

    @Test
    @DisplayName("Lanzar excepción ResourceNotFoundException si no encuentra al usuario.")
    void getUserByName_ThrowExceptionNotFound(){

        assertThrows(ResourceNotFoundException.class, () -> userRepository.getUserByName("userNoExistente"));
    }



}