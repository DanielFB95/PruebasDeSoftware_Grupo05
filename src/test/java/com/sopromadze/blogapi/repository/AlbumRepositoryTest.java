package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryTest {

    @MockBean
    AlbumRepository albumRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Album album;
    PageRequest pageRequest;
    User user;


    @BeforeEach
    void initData() {

        user = new User();
        user.setUsername("Pablo");
        user.setFirstName("Pablo");
        user.setLastName("Segura");
        user.setCreatedAt(Instant.now());

        album = new Album();

        album.setTitle("Album 1º");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        album.setUser(user);

        testEntityManager.persist(album);


    }

    @Test
    void whenfindByCreatedBy_success() {

        Page<Album> albums = new PageImpl<>(Arrays.asList(album));

        pageRequest =  PageRequest.of(1,2);

        Mockito.lenient().when(albumRepository.findByCreatedBy(user.getId(), pageRequest )).thenReturn(albums);

        assertNotEquals(0, albumRepository.findByCreatedBy(user.getId(), pageRequest).getTotalElements());


    }
}