package com.sopromadze.blogapi.config;

import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.h2.engine.Role;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean("customUserDetailsService")
    @Primary
    public UserDetailsService userDetailsService() {


        User admin = User.builder()
                .username("admin")
                .password("admin")
                .build();


        User user = User.builder()
                .username("user")
                .password("user")
                .build();
        UserPrincipal userPrincipal =
                new UserPrincipal(user.getId(), user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        UserPrincipal adminPrincipal =
                new UserPrincipal(admin.getId(), admin.getFirstName(),
                        admin.getLastName(),
                        admin.getUsername(),
                        admin.getEmail(),
                        admin.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));


        return new InMemoryUserDetailsManager(Arrays.asList(
                adminPrincipal, userPrincipal
        ));


    }


}