package com.supplytracker.controller;


import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.service.Imp.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping("/")
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return service.getUserById(id);
    }


    @PutMapping("/{id}/role={UserRole}")
    public UserResponseDto UpdateUser(@PathVariable Long id, @PathVariable String UserRole, @Valid  @RequestBody UserDto user){
        if(UserRole.toUpperCase().equals("ADMIN")){
            return service.UpdateUser(id,user);
        }
        throw new InvalidRoleException("The user with the current role cannot access this endpoint");

    }


    @DeleteMapping("/{id}")
    public String DeleteUserById(@PathVariable Long id){
        return service.DeleteUser(id);
    }


}
