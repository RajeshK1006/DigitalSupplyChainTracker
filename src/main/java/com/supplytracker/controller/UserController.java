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
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);



    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repo;

    private void Authorization(String email){
        User user = repo.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User with this email is not Found"));
        if(!"ADMIN".equals(String.valueOf(user.getRole()))){
            logger.warn("Unauthorized access attempt by the user with email: {}", email);
            throw new InvalidRoleException(("Only ADMIN'S are authorized to perform this action"));
        }
    }

    @GetMapping()
    public List<User> getAllUsers(@RequestParam String email){
        logger.info("Fetching all users");
        Authorization(email);
        return service.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(@RequestParam String email,@PathVariable Long id){
        logger.info("Fetching user with ID: {}", id);
        Authorization(email);
        return service.getUserById(id);
    }


    @PutMapping("/{id}/role")
    public UserResponseDto UpdateUser(@RequestParam String email,@Valid @PathVariable Long id ,@RequestBody UserDto user){
        logger.info("Updating user with ID: {} and role: {}", id, user.getRole());
        Authorization(email);

        return service.UpdateUser(id,user);



    }


    @DeleteMapping("/{id}")
    public String DeleteUserById(@RequestParam String email,@PathVariable Long id){

        logger.info("Deleting user with ID: {}", id);
        Authorization(email);
        return service.DeleteUser(id);
    }


}
