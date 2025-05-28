package com.supplytracker.service.interfaces;


import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.entity.User;

import java.util.List;

public interface UserServiceInterface {

    List<User> getAllUsers();
    User getUserById(Long id);
    UserResponseDto createUser(UserDto user);

    UserResponseDto UpdateUser(Long id, UserDto user);

    String DeleteUser(Long id);

    String LoginUser(LoginDto user);

}