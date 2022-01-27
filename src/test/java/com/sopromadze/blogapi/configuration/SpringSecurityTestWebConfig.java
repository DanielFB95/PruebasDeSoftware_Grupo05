package com.sopromadze.blogapi.configuration;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.*;

@TestConfiguration
public class SpringSecurityTestWebConfig {

    @Bean("customUserDetailsServiceImpl")
    @Primary
    public UserDetailsService userDetailsService(){

        Role roleAdmin = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        Role roleUser = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_USER)
                .build();

        User admin = User.builder()
                .id(1L)
                .firstName("Daniel")
                .lastName("Fernández")
                .username("danielfb")
                .password("12345")
                .email("danielfb@gmail.com")
                .address(null)
                .phone("620189675")
                .todos(new ArrayList<>())
                .albums(new ArrayList<>())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .company(new Company())
                .roles(Arrays.asList(roleAdmin))
                .build();

        User user = User.builder()
                .id(2L)
                .firstName("Daniel")
                .lastName("Fernández")
                .username("danielfb")
                .password("12345")
                .email("danielfb@gmail.com")
                .address(null)
                .phone("620189675")
                .todos(new ArrayList<>())
                .albums(new ArrayList<>())
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .company(new Company())
                .roles(Arrays.asList(roleUser))
                .build();

        return new InMemoryUserDetailsManager(List.of(admin,user));
    }

}
