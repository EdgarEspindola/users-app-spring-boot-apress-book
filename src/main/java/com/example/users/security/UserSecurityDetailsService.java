package com.example.users.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.users.exception.UserNotFoundException;
import com.example.users.model.User;
import com.example.users.model.UserRole;
import com.example.users.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserSecurityDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(UserNotFoundException::new);
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .roles(user.getUserRole().stream().map(UserRole::toString).toArray(String[]::new))
                .password(passwordEncoder.encode(user.getPassword()))
                .accountExpired(!user.isActive())
                .build();
    }
}
