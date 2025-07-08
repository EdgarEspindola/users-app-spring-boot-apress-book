package com.example.users.model;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash("USERS")
public class User {
    @Id
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Name can not be empty")
    private String name;

    private String gravatarUrl;

    @NotBlank(message = "Password can not be empty")
    private String password;

    @Singular("role")
    private Collection<UserRole> userRole;
    
    private boolean active;
}
