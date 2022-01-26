package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

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