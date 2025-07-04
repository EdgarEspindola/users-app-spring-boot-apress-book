package com.example.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApplicationTests {

	@Value("${local.server.port}")
	private int port;

	private final String BASE_URL = "http://localhost:";

	private final String USERS_PATH = "/users";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void indexPageShoulReturnHeaderOneContent() {
		assertThat(this.restTemplate.getForObject(BASE_URL + port, String.class))
				.contains("Simple Users Rest Application");
	}

	@Test
	public void usersEndPointShouldReturnCollectionWithTwoUsers() {
		Collection<User> response = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
		assertThat(response.size()).isEqualTo(2);
	} 

	@Test
	public void userEndPointPostNewUserShouldReturnUser() {
		User user = new User("dummy@email.com", "Dummy");
		User response = this.restTemplate.postForObject(BASE_URL + port + USERS_PATH, user, User.class);
		assertThat(response).isNotNull();
		assertThat(response.getEmail()).isEqualTo(user.getEmail());

		Collection<User> users = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
		assertThat(users.size()).isGreaterThanOrEqualTo(2);
	}

	@Test
	public void userEndPointDeleteUserShouldReturnVoid() {
		this.restTemplate.delete(BASE_URL + port + USERS_PATH + "/norma@email.com");

		Collection<User> users = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH, Collection.class);
		assertThat(users.size()).isLessThanOrEqualTo(2);
	}

	@Test
	public void userEndPointFindUserShouldReturnUser() {
		User user = this.restTemplate.getForObject(BASE_URL + port + USERS_PATH + "/ximena@email.com", User.class);
		assertThat(user).isNotNull();
		assertThat(user.getEmail()).isEqualTo("ximena@email.com");
	}

}
