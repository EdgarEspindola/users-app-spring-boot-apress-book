package com.example.users.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Singular;

@Builder
public record UserWithJdbcTemplate(Integer id, String email, String name, String password, boolean active, String gravatarUrl,
        @Singular("role") List<UserRole> userRole) {
    public UserWithJdbcTemplate {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);
        Objects.requireNonNull(password);
        Pattern pattern = Pattern.compile("\"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\\\S+$).{8,}$\"");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches())
            // throw new IllegalArgumentException(
            //         "Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character");

        pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        matcher = pattern.matcher(email);
        if (!matcher.matches())
            throw new IllegalArgumentException("Email must be a valid email address");

        if (gravatarUrl == null) {
            gravatarUrl = UserGravatar.getGravatarUrlFromEmail(email);
        }

        if (userRole == null) {
            userRole = new ArrayList<>(List.of(UserRole.INFO));
        }

    }

    public UserWithJdbcTemplate withId(Integer id) {
        return new UserWithJdbcTemplate(id, this.email(), this.name(), this.password(), this.active(), this.gravatarUrl(),
                this.userRole());
    }
}
