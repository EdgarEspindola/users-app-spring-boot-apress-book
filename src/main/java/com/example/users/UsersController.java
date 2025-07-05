package com.example.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    private Map<String, User> users = new HashMap<>() {
        {
            put("ximena@email.com", User.builder().email("ximena@email.com").name("Ximena").build());
            put("norma@email.com", User.builder().email("norma@email.com").name("Norma").build());
        }
    };

    @GetMapping
    public Collection<User> getAll() {
        return this.users.values();
    }

    @GetMapping("/{email}")
    public User findUserByEmail(@PathVariable String email) {
        return this.users.get(email);
    }

    @PostMapping
    public User save(@RequestBody User user) {
        this.users.put(user.getEmail(), user);
        return user;
    }

    @DeleteMapping("/{email}")
    public void save(@PathVariable String email) {
        this.users.remove(email);
    }
}
