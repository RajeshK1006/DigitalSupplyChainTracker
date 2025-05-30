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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("rajesh@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.SUPPLIER);

        userDto = new UserDto();
        userDto.setEmail("rajesh@example.com");
        userDto.setPassword("Rajesh@123");
        userDto.setRole("SUPPLIER");

        responseDto = new UserResponseDto();
        responseDto.setEmail("rajesh@example.com");
        responseDto.setRole("SUPPLIER");
    }



    @Test
    void testCreateUser_Success() {
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(encoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(repo.save(any(User.class))).thenReturn(user);
        when(mapper.map(user, UserResponseDto.class)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(userDto);
        assertEquals(responseDto.getEmail(), result.getEmail());
    }



    @Test
    void testUpdateUser_InvalidRole() {
        userDto.setRole("INVALID_ROLE");
        when(repo.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(InvalidRoleException.class, () -> userService.UpdateUser(1L, userDto));
    }


    @Test
    void testLoginUser_Success() {
        LoginDto loginDto = new LoginDto("rajesh@example.com", "Rajesh@123");
        when(repo.findByEmail("rajesh@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("Rajesh@123", "encodedPassword")).thenReturn(true);
        String result = userService.LoginUser(loginDto);
        assertEquals("Login Successful", result);
    }


}
