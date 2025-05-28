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

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repo;

    @GetMapping()
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return service.getUserById(id);
    }


    @PutMapping("/{id}/role")
    public UserResponseDto UpdateUser(@Valid @PathVariable Long id ,@RequestBody UserDto user){
       User current = repo.findById(id).orElseThrow(()-> new UserNotFoundException("Te user with this id is not Found"));
       if(current != null){
           if(current.getRole().equals("ADMIN")){
               return service.UpdateUser(id,user);
           }
       }
        throw new InvalidRoleException("The user with the current role cannot access the endpoinit");

    }


    @DeleteMapping("/{id}")
    public String DeleteUserById(@PathVariable Long id){
        return service.DeleteUser(id);
    }


}
