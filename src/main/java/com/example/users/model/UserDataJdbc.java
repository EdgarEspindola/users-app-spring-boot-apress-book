package com.example.users.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("USERS")
public class UserDataJdbc {
    @Id
    Long id;

    @NotBlank(message = "Email can not be empty")
    private String email;
    
    @NotBlank(message = "Name can not be empty")
    private String name;
    
    private String gravatarUrl;
    
    @Pattern(message = "Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;
    
    @Singular("role")
    private List<UserRole> userRole;
    
    private boolean active;
}
