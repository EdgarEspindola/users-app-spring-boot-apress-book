package com.example.users;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
@Data
public class User {
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Name can not be empty")
    private String name;
    
    private String gravatarUrl;
    
    @Pattern(message = "Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;
    
    @Singular("role")
    private List<UserRole> userRole;
    
    private boolean active;
}
