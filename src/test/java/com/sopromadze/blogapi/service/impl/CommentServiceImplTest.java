package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostRepository postRepository;


    //  TestEntityManager testEntityManager;


    //Constantes de respuesta

    private static final String ID_STR = "id";

    private static final String COMMENT_STR = "Comment";

    private static final String POST_STR = "Post";

    //Constantes de respuesta


    User user, anotherUser, admin;
    Comment comment, anotherComment;
    Post post, anotherPost;

    CommentRequest commentRequest;
    UserPrincipal userPrincipal, anotherUserPrincipal, adminPrincipal;

    @BeforeEach
    void initData() {

        //commentService = new CommentServiceImpl(commentRepository, postRepository, userRepository);

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

        anotherUser = User.builder()
                .id(2L)
                .firstName("Ana Maria")
                .lastName("Galan")
                .username("Anamari76")
                .password("micontrasenaespecial")
                .email("anamari76@gmail.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        admin = User.builder()
                .id(3L)
                .firstName("Luisa")
                .lastName("Fernandez")
                .username("AdminDelBlog")
                .password("micontrasenaespecial")
                .email("yosoyadmin@admin.com")
                .comments(new ArrayList<>())
                .roles(new ArrayList<>())
                .build();

        user.getRoles().add(new Role(RoleName.ROLE_USER));
        anotherUser.getRoles().add(new Role(RoleName.ROLE_USER));
        admin.getRoles().add(new Role(RoleName.ROLE_ADMIN));

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

        anotherPost = Post.builder()
                .id(2L)
                .title("Os voy a hablar de una experiencia inolvidable")
                .body("Lorem fistrum a wan apetecan ese hombree ese que llega ese que llega a gramenawer. Caballo blanco caballo negroorl")
                .comments(new ArrayList<>())
                .build();


        anotherComment = Comment.builder()
                .id(2L)
                .body("Lucas caballo blanco caballo negroorl a peich")
                .name(user.getFirstName())
                .user(user)
                .email(user.getEmail())
                .build();

        post.getComments().add(comment);
        user.getComments().add(comment);
        comment.setPost(post);

        commentRequest = new CommentRequest();

        userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());
        anotherUserPrincipal = new UserPrincipal(anotherUser.getId(), anotherUser.getFirstName(), anotherUser.getLastName(), anotherUser.getUsername(), anotherUser.getEmail(), anotherUser.getPassword(), new ArrayList<>());
        adminPrincipal = new UserPrincipal(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getUsername(), admin.getEmail(), admin.getPassword(), Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));
    }

    @Test
    @DisplayName("Prueba de funcionamiento correcto de getComment")
    public void getComment_success() {


        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        assertEquals(comment, commentService.getComment(post.getId(), comment.getId()));

    }

    @ParameterizedTest
    @MethodSource("getMockIds")
    @DisplayName("Devuelve excepcion ResourceNotFound si no encuentra el post o el comentario")
    public void getCommentNotFound_resourceNotFoundException(Long postId, Long commentId) {

        Long idInexistente = 0L;

        lenient().when(postRepository.findById(idInexistente)).thenThrow(new ResourceNotFoundException(POST_STR, ID_STR, idInexistente));
        lenient().when(commentRepository.findById(idInexistente)).thenThrow(new ResourceNotFoundException(COMMENT_STR, ID_STR, idInexistente));
        assertThrows(ResourceNotFoundException.class, () -> commentService.getComment(postId, commentId));

    }

    private static Stream<Arguments> getMockIds() {
        return Stream.of(
                Arguments.arguments(1L, 0L), //Post existente, pero comentario no
                Arguments.arguments(0L, 1L) //Comentario existente, pero post no
        );
    }


    //Devolucion de excepcion BlogapiException
    @Test
    public void getCommentWrongPost_blogApiException() {

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(anotherPost));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        //Post no relacionado con el comentario buscado
        assertThrows(BlogapiException.class, () -> commentService.getComment(anotherPost.getId(), comment.getId()));


    }


    ////////////


    //Funcionamiento correcto de updateComment (COMO USUARIO Y COMO ADMIN)
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Editar comentario como USER y como ADMIN con éxito")
    public void updateComment_success(int index) {

        UserPrincipal currentUser;
        List<UserPrincipal> mockUsers = Arrays.asList(userPrincipal, adminPrincipal);
        currentUser = mockUsers.get(index);

        String newBody = "Rectificar es bueno, por eso cambio este comentario.";

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        commentRequest.setBody(newBody);
        commentService.updateComment(post.getId(),
                comment.getId(),
                commentRequest,
                currentUser);

        //Nuevo comentario ---- Cuerpo del comentario buscado por ID
        assertEquals(newBody, commentService.getComment(post.getId(), comment.getId()).getBody());

    }


    //Usuario no logueado tratando de editar un comentario
    @Test
    @DisplayName("Lanzar excepción si un USER edita un comentario de otro")
    public void updateCommentUnathorized_blogApiException() {


        String newBody = "Intentando cambiar un comentario de otra persona.";

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        commentRequest.setBody(newBody);

        assertThrows(BlogapiException.class, () ->
                commentService.updateComment(post.getId(),
                        comment.getId(),
                        commentRequest,
                        anotherUserPrincipal));

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void deleteComment_success(int index) {


        UserPrincipal currentUser;
        List<UserPrincipal> mockUsers = Arrays.asList(userPrincipal, adminPrincipal);
        currentUser = mockUsers.get(index);


        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        ApiResponse apiResponse = commentService.deleteComment(post.getId(), comment.getId(), currentUser);


        assertTrue(apiResponse.getSuccess());

    }


}