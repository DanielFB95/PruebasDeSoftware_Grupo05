package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

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
    Comment comment;
    Post post;

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

    @Test
    public void whenGetCommentFindPostAndComment_success() {


        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        assertEquals(comment, commentService.getComment(post.getId(), comment.getId()));

    }

    //Devolucion de excepcion ResourceNotFound
    @Test
    public void whenGetCommentPostOrCommentNotFound_throwResourcetNotFoundException() {

        Long idInexistente = 0L;

        //when(commentRepository.findById(idInexistente)).thenReturn(null);

            when(postRepository.findById(idInexistente)).thenThrow(new ResourceNotFoundException(POST_STR, ID_STR, idInexistente));
            //assertThrows(ResourceNotFoundException)

    }


    //Devolucion de excepcion BlogapiException


}