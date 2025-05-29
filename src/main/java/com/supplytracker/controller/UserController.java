package com.supplytracker.controller;


import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
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

    @GetMapping()
    public List<User> getAllUsers(){
        logger.info("Fetching all users");
        return service.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        logger.info("Fetching user with ID: {}", id);
        return service.getUserById(id);
    }


    @PutMapping("/{id}/role")
    public UserResponseDto UpdateUser(@Valid @PathVariable Long id ,@RequestBody UserDto user){
        logger.info("Updating user with ID: {} and role: {}", id, user.getRole());
        User current = repo.findById(id).orElseThrow(() -> {
            logger.error("User with ID {} not found", id);
            return new UserNotFoundException("The user with this ID is not found");
        });
       if(current != null){
           if(current.getRole().equals("ADMIN")){
               return service.UpdateUser(id,user);
           }
       }
        logger.warn("Unauthorized role access attempt by user with ID: {}", id);
        throw new InvalidRoleException("The user with the current role cannot access the endpoinit");

    }


    @DeleteMapping("/{id}")
    public String DeleteUserById(@PathVariable Long id){

        logger.info("Deleting user with ID: {}", id);
        return service.DeleteUser(id);
    }


}
