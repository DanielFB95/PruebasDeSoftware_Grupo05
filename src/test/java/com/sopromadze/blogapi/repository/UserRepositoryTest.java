package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;



import org.springframework.test.context.ActiveProfiles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;



@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired

     UserRepository userRepository;

    @Autowired
     TestEntityManager testEntityManager;



    User user, userNoExiste;
    UserPrincipal userPrincipal, userPrincipalNoExiste;

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

        userNoExiste = new User();

        userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());
        userPrincipalNoExiste = new UserPrincipal(userNoExiste.getId(), userNoExiste.getFirstName(), userNoExiste.getLastName(), userNoExiste.getUsername(), userNoExiste.getEmail(), userNoExiste.getPassword(), new ArrayList<>());

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

    @Test
    void whenFoundUser_existsByEmail_success() {

        //when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertTrue(userRepository.existsByEmail(user.getEmail()));

    }

    @Test
    void whenGetUser_success(){

        assertEquals(user, userRepository.getUser(userPrincipal));
    }

    @Test
    void whenGetUserNotFound_ThrowExceptionNotFound(){

        assertThrows(ResourceNotFoundException.class, () -> userRepository.getUser(userPrincipalNoExiste));
    }





}