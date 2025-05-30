package com.supplytracker.controller;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repo;

    // Authorize only ADMIN users
    private void authorizeAdmin(String email){
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email is not found"));
        if(!"ADMIN".equals(String.valueOf(user.getRole()))){
            logger.warn("Unauthorized access attempt");
            throw new InvalidRoleException("Only ADMINs are authorized to perform this action");
        }
    }

    // Get all users - only for ADMIN
    @GetMapping()
    public List<User> getAllUsers(@RequestParam String email){
        logger.info("Fetching all users");
        authorizeAdmin(email);
        return service.getAllUsers();
    }

    // Get user by ID - only for ADMIN
    @GetMapping("/{id}")
    public User getUserById(@RequestParam String email, @PathVariable Long id){
        logger.info("Fetching user by ID");
        authorizeAdmin(email);
        return service.getUserById(id);
    }

    // Update user role - only for ADMIN
    @PutMapping("/{id}/role")
    public UserResponseDto updateUser(@RequestParam String email, @Valid @PathVariable Long id, @RequestBody UserDto user){
        logger.info("Updating user role");
        authorizeAdmin(email);
        return service.UpdateUser(id, user);
    }

    // Delete user by ID - only for ADMIN
    @DeleteMapping("/{id}")
    public String deleteUserById(@RequestParam String email, @PathVariable Long id){
        logger.info("Deleting user");
        authorizeAdmin(email);
        return service.DeleteUser(id);
    }
}
