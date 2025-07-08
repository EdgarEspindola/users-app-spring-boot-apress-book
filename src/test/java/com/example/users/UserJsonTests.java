package com.example.users;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import com.example.users.model.User;
import com.example.users.model.UserGravatar;
import com.example.users.model.UserRole;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;



@JsonTest
public class UserJsonTests {
    @Autowired
    private JacksonTester<User> jacksonTester;

    @Test
    void serializeUserJsonTest() throws IOException {
        User user = User.builder()
            .email("dummy@email.com")
            .password("aw2s0me")
            .name("Dummy")
            .role(UserRole.USER)
            .active(true)
            .build();

        JsonContent<User> json = jacksonTester.write(user);
        assertThat(json).extractingJsonPathValue("$.email").isEqualTo("dummy@email.com");
        assertThat(json).extractingJsonPathArrayValue("$.userRole").size().isEqualTo(1);
        assertThat(json).extractingJsonPathBooleanValue("$.active").isTrue();
        assertThat(json).extractingJsonPathValue("$.gravatarUrl").isNotNull();
        assertThat(json).extractingJsonPathValue("$.gravatarUrl").isEqualTo(UserGravatar.getGravatarUrlFromEmail(user.getEmail()));
    }


    @Test
    void serializeUserJsonFileTest() throws IOException{
        User user = User.builder()
            .email("dummy@email.com")
            .password("aw2s0me")
            .name("Dummy")
            .role(UserRole.USER)
            .active(true)
            .build();
        System.out.println(user);
        
        JsonContent<User> json =  jacksonTester.write(user);
        assertThat(json).isEqualToJson("user.json");
    }

    @Test
    void deserializeUserJsonTest() throws Exception{
        String userJson = """
                {
                  "email": "dummy@email.com",
                  "name": "Dummy",
                  "password": "aw2s0me",
                  "userRole": ["USER"],
                  "active": true
                }
                """;
        User user = this.jacksonTester.parseObject(userJson);
        assertThat(user.getEmail()).isEqualTo("dummy@email.com");
        assertThat(user.getPassword()).isEqualTo("aw2s0me");
        assertThat(user.isActive()).isTrue();
    }

    // @Test
    // void userValidationTest(){
    //     assertThatExceptionOfType(ConstraintViolationException.class)
    //             .isThrownBy( () -> UserBuilder.createUser(Validation.buildDefaultValidatorFactory().getValidator())
    //                     .withEmail("dummy@email.com")
    //                     .withName("Dummy")
    //                     .withRoles(UserRole.USER)
    //                     .active().build());
    //     // Junit 5
    //     Exception exception = assertThrows(ConstraintViolationException.class, () -> {
    //         UserBuilder.createUser(Validation.buildDefaultValidatorFactory().getValidator())
    //                 .withName("Dummy")
    //                 .withRoles(UserRole.USER)
    //                 .active().build();
    //     });
    //     String expectedMessage = "email: Email can not be empty";
    //     assertThat(exception.getMessage()).contains(expectedMessage);
    // }
}
