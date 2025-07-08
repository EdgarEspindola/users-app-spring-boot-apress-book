package com.example.users.model;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name="USERS")
public class UserDataJpa {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

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

    @PrePersist
    private void prePersist(){
    if (this.gravatarUrl == null)
        this.gravatarUrl = UserGravatar.getGravatarUrlFromEmail(this.email);
    
    if(this.userRole == null)
        this.userRole = Collections.singletonList(UserRole.INFO);
    }
}
