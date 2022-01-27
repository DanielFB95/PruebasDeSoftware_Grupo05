package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.configuration.SpringSecurityTestWebConfig;
import lombok.extern.java.Log;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes ={SpringSecurityTestWebConfig.class} )
@AutoConfigureMockMvc
@Log
class CategoryControllerTest {



}