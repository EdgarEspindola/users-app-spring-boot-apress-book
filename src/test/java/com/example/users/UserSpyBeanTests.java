package com.example.users;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.users.model.User;
import com.example.users.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("spyBean")
public class UserSpyBeanTests {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> mockUsers = Arrays.asList(
                User.builder()
                        .name("Ximena")
                        .email("ximena@email.com")
                        .build(),
                User.builder()
                        .name("Norma")
                        .email("norma@email.com")
                        .build()
        );
        doReturn(mockUsers).when(userRepository).findAll();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ximena"))
                .andExpect(jsonPath("$[1].name").value("Norma"));
        verify(userRepository).findAll();
    }
}
