package com.example.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.users.repository.UserRepository;


@Configuration
public class UserSecurityConfigCustomSecurity {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests( auth -> auth.anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new UserSecurityDetailsService(userRepository));
        return provider;
    }
}
