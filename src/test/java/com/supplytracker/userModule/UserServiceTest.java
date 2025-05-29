package com.supplytracker.userModule;


import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void testCreateUser_Success() {

        UserDto dto = new UserDto("Alice", "alice@example.com", "Password@123", "SUPPLIER");

        User mappedUser = new User();
        mappedUser.setName("Alice");
        mappedUser.setEmail("alice@example.com");
        mappedUser.setRole(Role.SUPPLIER); // Must match enum
        mappedUser.setPassword("encodedPass");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName("Alice");
        responseDto.setEmail("alice@example.com");

        when(mapper.map(eq(dto), eq(User.class))).thenReturn(mappedUser);
        when(encoder.encode("Password@123")).thenReturn("encodedPass");
        when(repo.save(any(User.class))).thenReturn(mappedUser);
        when(mapper.map(eq(mappedUser), eq(UserResponseDto.class))).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());

        verify(repo).save(any(User.class));
    }

    @Test
    void testGetUserById_ValidId() {
        User user = new User();
        user.setId(1L);
        user.setName("John");

        when(repo.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);
        assertEquals("John", result.getName());
    }

    @Test
    void testGetUserById_InvalidId_ThrowsException() {
        when(repo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(100L));
    }

    @Test
    void testLoginUser_Success() {
        LoginDto loginDto = new LoginDto("john@example.com", "12345678");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("encoded123");

        when(repo.findByEmailIgnoreCase("john@example.com")).thenReturn(user);
        when(encoder.matches("12345678", "encoded123")).thenReturn(true);

        String result = userService.LoginUser(loginDto);
        assertEquals("Login Successful", result);
    }
}

