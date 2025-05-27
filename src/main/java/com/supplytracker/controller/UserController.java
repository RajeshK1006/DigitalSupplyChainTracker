package com.supplytracker.controller;


import com.supplytracker.dto.UserDto;
import com.supplytracker.entity.User;
import com.supplytracker.service.UserService;
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


    @PutMapping("/{id}/role")
    public UserDto UpdateUser(@PathVariable Long id,@Valid  @RequestBody UserDto user){
        return service.UpdateUser(id,user);
    }


    @DeleteMapping("/{id}")
    public String DeleteUserById(@PathVariable Long id){
        return service.DeleteUser(id);
    }


}
