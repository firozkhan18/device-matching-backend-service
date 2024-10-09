package com.springboot.microservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.microservice.entity.User;
import com.springboot.microservice.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(Integer.valueOf(id)); // Convert to Integer
    }
    
    public void removeUserById(int id) {
        userRepository.deleteById(id);
    }
}
