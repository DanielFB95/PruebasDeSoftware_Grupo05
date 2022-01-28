package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import java.util.Arrays;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Photo foto;
    Album album;
    Page<Album> paginaPhotos;

    @BeforeEach
    void setUp() {

        album = new Album();
        album.setId(1L);
        album.setTitle("album");

        foto = Photo.builder()
                .id(1L)
                .title("foto")
                .url("www.url.com")
                .thumbnailUrl("www.otraUrl.com")
                .album(album)
                .build();

        album.setPhoto(Arrays.asList(foto));

        paginaPhotos = new PageImpl<>(Arrays.asList(album));


    }

    @Test
    void findByAlbumId_success() {



    }
}