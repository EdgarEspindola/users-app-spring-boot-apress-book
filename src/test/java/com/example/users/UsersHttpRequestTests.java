package com.example.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersHttpRequestTests {
    @Value("${local.server.port}")
    private int port;

    private final String BASE_URL = "http://localhost:";
    private final String USERS_PATH = "/users";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageShouldReturnHeaderOneContent() throws Exception {
        assertThat(this.restTemplate.getForObject(BASE_URL + port,
                String.class)).contains("Simple Users Rest Application");
    }
    
    @Test
    public void usersEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<UserWithJdbcTemplate> response = this.restTemplate
            .getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(response.size()).isGreaterThanOrEqualTo(2);
    }

    // @Test
    // public void shouldReturnErrorWhenPostBadUserForm() throws Exception {
    //     assertThatThrownBy(() -> {
    //         UserWithJdbcTemplate user =  UserWithJdbcTemplate.builder()
    //                 .email("bademail")
    //                 .name("Dummy")
    //                 .active(true)
    //                 .password("aw2s0")
    //                 .build();
    //     }).isInstanceOf(IllegalArgumentException.class)
    //             .hasMessageContaining("Password must be at least 8 characters long and contain at least one number, one uppercase, one lowercase and one special character");
    // }

    @Test
    public void userEndPointPostNewUserShouldReturnUser() throws Exception {
        User user =  User.builder()
                .email("dummy@email.com")
                .name("Dummy")
                .gravatarUrl("https://www.gravatar.com/avatar/23bb62a7d0ca63c9a804908e57bf6bd4?d=wavatar")
                .password("aw2s0meR!")
                .active(true)
                .role(UserRole.USER)
                .build();
        User response =  this.restTemplate.postForObject(BASE_URL + port + USERS_PATH,user,User.class);
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        
        Collection<User> users = this.restTemplate.
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void userEndPointDeleteUserShouldReturnVoid() throws Exception {
        this.restTemplate.delete(BASE_URL + port + USERS_PATH + "/norma@email.com");
       
        Collection<UserWithJdbcTemplate> users = this.restTemplate.
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);
        assertThat(users.size()).isLessThanOrEqualTo(2);
    }

    @Test
    public void userEndPointFindUserShouldReturnUser() throws Exception{
        User user = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH + "/ximena@email.com",User.class);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("ximena@email.com");
    }

}
