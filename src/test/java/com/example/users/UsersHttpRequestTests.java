package com.example.users;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.example.users.model.User;
import com.example.users.model.UserRole;
import com.example.users.model.UserWithJdbcTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersHttpRequestTests {
   
    private final String USERS_PATH = "/users";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageShouldReturnHeaderOneContent() throws Exception {
        String html = this.restTemplate.withBasicAuth("manager@email.com","aw2s0meR!").getForObject("/", String.class);
        assertThat(html).contains("Simple Users Rest Application");
    }
    
    @Test
    public void usersEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<User> response = this.restTemplate
            .withBasicAuth("manager@email.com","aw2s0meR!")
            .getForObject(USERS_PATH, Collection.class);
        assertThat(response.size()).isGreaterThan(1);
    }

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
        User response =  this.restTemplate
            .withBasicAuth("manager@email.com","aw2s0meR!")
            .postForObject(USERS_PATH,user,User.class);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        
        Collection<User> users = this.restTemplate
                .withBasicAuth("manager@email.com","aw2s0meR!")
                .getForObject(USERS_PATH, Collection.class);

        assertThat(users.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void userEndPointDeleteUserShouldReturnVoid() throws Exception {
        this.restTemplate
            .withBasicAuth("manager@email.com","aw2s0meR!")
            .delete(USERS_PATH + "/norma@email.com");
       
        Collection<UserWithJdbcTemplate> users = this.restTemplate
            .withBasicAuth("manager@email.com","aw2s0meR!")    
            .getForObject(USERS_PATH, Collection.class);

        assertThat(users.size()).isLessThanOrEqualTo(2);
    }

    @Test
    public void userEndPointFindUserShouldReturnUser() throws Exception{
        User user = this.restTemplate
            .withBasicAuth("manager@email.com","aw2s0meR!")
            .getForObject(USERS_PATH + "/ximena@email.com",User.class);
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("ximena@email.com");
    }

}
