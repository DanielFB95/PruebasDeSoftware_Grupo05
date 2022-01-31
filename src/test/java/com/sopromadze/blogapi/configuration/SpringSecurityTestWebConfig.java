package com.sopromadze.blogapi.configuration;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())))
                .build();

        UserPrincipal adminPrincipal = UserPrincipal.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .email(admin.getEmail())
                .authorities(Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())))
                .build();

        new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString());


        return new InMemoryUserDetailsManager(List.of(userPrincipal, adminPrincipal));
    }

}
