package com.example.users;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UserConfiguration {
    @Bean
    ApplicationListener<ApplicationReadyEvent> init(UserRepository userRepository) {
        return applicationReadyEvent -> {
            User ximena = User.builder()
                            .email("ximena@email.com")
                            .name("Ximena")
                            .gravatarUrl("https://www.gravatar.com/avatar/23bb62a7d0ca63c9a804908e57bf6bd4?d=wavatar")
                            .password("aw2s0meR!")
                            .active(true)
                            .role(UserRole.USER)
                            .build();
            userRepository.save(ximena);

            User norma = User.builder()
                    .email("norma@email.com")
                    .name("Norma")
                    .gravatarUrl("https://www.gravatar.com/avatar/f07f7e553264c9710105edebe6c465e7?d=wavatar")
                    .password("aw2s0meR!")
                    .active(true)
                    .role(UserRole.USER)
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(norma);
        };
    }
}
