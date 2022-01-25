package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Mock
    UserRepository userRepository;

    //Constantes de respuesta
    private static final String THIS_COMMENT = " this comment";

    private static final String YOU_DON_T_HAVE_PERMISSION_TO = "You don't have permission to ";

    private static final String ID_STR = "id";

    private static final String COMMENT_STR = "Comment";

    private static final String POST_STR = "Post";

    private static final String COMMENT_DOES_NOT_BELONG_TO_POST = "Comment does not belong to post";

    //Constantes de respuesta


    User user;
    Comment comment, anotherComment;
    Post post, anotherPost;

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


    }

    //Funcionamiento correcto de getComment

    @Test
    public void getComment_success() {


        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        assertEquals(comment, commentService.getComment(post.getId(), comment.getId()));

    }

    //Devolucion de excepcion ResourceNotFound
    @ParameterizedTest
    @MethodSource("getMockIds")
    public void getCommentNotFound_resourceNotFoundException(Long postId, Long commentId) {

        Long idInexistente = 0L;

        lenient().when(postRepository.findById(idInexistente)).thenThrow(new ResourceNotFoundException(POST_STR, ID_STR, idInexistente));
        lenient().when(commentRepository.findById(idInexistente)).thenThrow(new ResourceNotFoundException(COMMENT_STR, ID_STR, idInexistente));
        assertThrows(ResourceNotFoundException.class, () -> commentService.getComment(postId, commentId));

    }

    private static Stream<Arguments> getMockIds(){
        return Stream.of(
                Arguments.arguments(1L, 0L), //Post existente, pero comentario no
                Arguments.arguments(0L, 1L) //Comentario existente, pero post no
        );
    }


    //Devolucion de excepcion BlogapiException
    @Test
    public void getCommentWrongPost_blogApiNotFoundException(){

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(anotherPost));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        //Post no relacionado con el comentario buscado
        assertThrows(BlogapiException.class, () -> commentService.getComment(anotherPost.getId(), comment.getId()));

    }



    ////////////

    @Test
    public void updateComment_success(){

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setBody("Rectificar es bueno, por eso cambio este comentario.");

        UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), new ArrayList<>());

        Comment commentEdited = commentService.updateComment(post.getId(),
                comment.getId(),
                commentRequest,
                userPrincipal);
        //TODO: SOLUCIONAR:  Cannot invoke "com.sopromadze.blogapi.model.Comment.getBody()" because "commentEdited" is null
        assertTrue(commentEdited.getBody() == commentRepository.findById(comment.getId()).get().getBody() );

    }

}