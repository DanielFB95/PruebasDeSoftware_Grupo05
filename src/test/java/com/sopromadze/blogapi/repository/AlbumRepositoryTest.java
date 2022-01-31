package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryTest {

    @Mock
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Album album;


    @BeforeEach
    void initData() {

        album = new Album();

        album.setTitle("Album 1º");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        testEntityManager.persist(album);



    }

    @Test
    void whenfindByCreatedBy_success() {

        List<Album> albums = new ArrayList<>();
        albums.add(album);

        User user = new User();
        user.setUsername("Pablo");
        user.setFirstName("Pablo");
        user.setLastName("Segura");
        user.setAlbums(albums);
        user.setCreatedAt(Instant.now());



        PageRequest pageable =  PageRequest.of(1,2);


        assertNotEquals(0, albumRepository.findByCreatedBy(user.getId(), pageable).getTotalElements());


    }
}