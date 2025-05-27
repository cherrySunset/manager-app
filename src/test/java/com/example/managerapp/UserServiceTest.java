package com.example.managerapp;

import com.example.managerapp.model.User;
import com.example.managerapp.repository.UserRepository;
import com.example.managerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void shouldCreateUser() {
        User user = new User(null, "Alice", "alice@example.com", "USER");
        User savedUser = new User(1L, "Alice", "alice@example.com", "USER");

        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldGetUserById() {
        User user = new User(1L, "Bob", "bob@example.com", "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(1L);

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getName());
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "Alice", "alice@example.com", "USER"),
                new User(2L, "Bob", "bob@example.com", "ADMIN")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldUpdateUser() {
        User existing = new User(1L, "Alice", "alice@example.com", "USER");
        User updated = new User(1L, "Alice Smith", "alice.smith@example.com", "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(updated);

        User result = userService.updateUser(1L, updated);

        assertEquals("Alice Smith", result.getName());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    public void shouldDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
