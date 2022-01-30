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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhotoRepositoryTest {

    @MockBean
    private PhotoRepository photoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Photo foto;
    Album album;
    Page<Photo> paginaPhotos;
    Pageable pageable;

    @BeforeEach
    void setUp() {

        album = Album.builder()
                .id(1L)
                .title("Album")
                .build();

        foto = Photo.builder()
                .id(1L)
                .title("foto")
                .url("www.url.com")
                .thumbnailUrl("www.otraUrl.com")
                .album(album)
                .build();

        album.setPhoto(Arrays.asList(foto));

        paginaPhotos = new PageImpl<>(Arrays.asList(foto));

        pageable = PageRequest.of(1,1);

    }

    @Test
    void findByAlbumId_success() {

        when(photoRepository.findByAlbumId(any(Long.class),any(Pageable.class))).thenReturn(paginaPhotos);
        assertEquals(Arrays.asList(foto),photoRepository.findByAlbumId(1L,pageable).getContent());
    }
}