package com.supplytracker.service.Imp;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    List<User> getAllUsers();
    User getUserById(Long id);
    UserDto createUser(UserDto user);

    UserDto UpdateUser(Long id, UserDto user);

    String DeleteUser(Long id);

    String LoginUser(LoginDto user);

}
