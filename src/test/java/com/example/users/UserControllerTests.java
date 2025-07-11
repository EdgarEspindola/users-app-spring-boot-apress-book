package com.example.users;

import com.example.users.model.User;
import com.example.users.model.UserRole;
import com.example.users.repository.UserRepository;
import com.example.users.web.UsersController;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { UsersController.class })
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getAllUsersTest() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                User.builder()
                        .name("Ximena")
                        .email("ximena@email.com")
                        .active(true)
                        .role(UserRole.USER)
                        .role(UserRole.ADMIN)
                        .password("aw3s0m3R!")
                        .build(),
                User.builder()
                        .name("Norma")
                        .email("norma@email.com")
                        .active(true)
                        .role(UserRole.USER)
                        .password("aw3s0m3R!")
                        .build()
        ));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void newUserTest() throws Exception {
        User user = User.builder()
                        .name("Ximena")
                        .email("ximena@email.com")
                        .active(true)
                        .role(UserRole.USER)
                        .role(UserRole.ADMIN)
                        .password("aw3s0m3R!")
                        .build();

        when(userRepository.save(user)).thenReturn(user);
        
        mockMvc.perform(post("/users")
                        .content(toJson(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("dummy@email.com"));
    }

    @Test
    void findUserByEmailTest() throws Exception {
        User user = User.builder()
                        .name("Ximena")
                        .email("ximena@email.com")
                        .active(true)
                        .role(UserRole.USER)
                        .role(UserRole.ADMIN)
                        .password("aw3s0m3R!")
                        .build();

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
       
        mockMvc.perform(get("/users/{email}",user.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("dummy@email.com"));
    }

    @Test
    void deleteUserByEmailTest() throws Exception{
        User user = User.builder()
                .email("dummy@email.com")
                .build();

        doNothing().when(userRepository).deleteById(user.getEmail());
        
        mockMvc.perform(delete("/users/{email}",user.getEmail()))
                .andExpect(status().isNoContent());
    }

    private static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
