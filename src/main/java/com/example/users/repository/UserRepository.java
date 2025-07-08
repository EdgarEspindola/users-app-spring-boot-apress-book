package com.example.users.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.users.model.User;

public interface UserRepository extends CrudRepository<User, String> {
    
}
