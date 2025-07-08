package com.example.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

//@Configuration
public class UserSecurityConfigInMemory {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests( auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder){
        UserDetails admin = User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN","USER")
                .build();

        UserDetails manager = User
                .builder()
                .username("manager@email.com")
                .password(passwordEncoder.encode("aw2s0meR!"))
                .roles("ADMIN","USER")
                .build();

        UserDetails user = User
                .builder()
                .username("user@email.com")
                .password(passwordEncoder.encode("aw2s0meR!"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(manager,user,admin);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
