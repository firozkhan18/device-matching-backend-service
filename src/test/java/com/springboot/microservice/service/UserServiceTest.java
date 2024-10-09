package com.springboot.microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.springboot.microservice.entity.User;
import com.springboot.microservice.repository.UserRepository;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService();
        // Use reflection to inject the mock UserRepository into UserService
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("Test User", savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUser("1");

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testRemoveUserById() {
        int userId = 1;

        userService.removeUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
