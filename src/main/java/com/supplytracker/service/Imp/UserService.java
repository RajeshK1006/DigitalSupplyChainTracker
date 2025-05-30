package com.supplytracker.service.Imp;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.interfaces.UserServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserServiceInterface {


    private static final Logger logger =LoggerFactory.getLogger((UserService.class));
    @Autowired
    public ModelMapper mapper;
    @Autowired
    public UserRepository repo;
    @Autowired
    public PasswordEncoder encoder;


    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users from the database.");

        return repo.findAll();
    }

    @Override
    public User getUserById(Long id) {

        logger.info("Fetching user by ID: {}", id);
        return repo.findById(id).orElseThrow(()-> {
            logger.error("User with Id {} not found",id);
            return new UserNotFoundException("The user with this id: "+ id +" not found");
        });
    }



    @Override
    public UserResponseDto createUser(UserDto user) {

        logger.info("Creating a new user with email: {}", user.getEmail());
        User newuser = mapper.map(user, User.class);
        try{
            newuser.setRole(Role.valueOf(user.getRole().toUpperCase()));
        }
        catch (IllegalArgumentException ex){
            logger.error("Invalid role provided: {}", user.getRole());
            throw new InvalidRoleException("Invalid Role Exception: "+ user.getRole());
        }

           newuser.setPassword(encoder.encode(user.getPassword()));
           repo.save(newuser);
           logger.info("Updating user with ID: {}", newuser.getId());
           return mapper.map(newuser, UserResponseDto.class);

    }

    @Override
    public UserResponseDto UpdateUser(Long id, UserDto user) {
        logger.info("Updating user with Id: {}", id);
        User existing = repo.findById(id).orElseThrow(()-> {
            logger.error("User with ID {} not found for update", id);
            return new UserNotFoundException("User with this id: "+id+" not found");});
        try{
            if(user.getName()!=null) existing.setName(user.getName());
            if (user.getEmail()!=null)existing.setEmail(user.getEmail());
            if(user.getPassword()!=null) existing.setPassword(encoder.encode(user.getPassword()));

            if(user.getRole()!=null) existing.setRole(Role.valueOf(user.getRole().toUpperCase()));
        }
        catch(IllegalArgumentException ex){
            logger.error("Invalid role provided while updating: {}",user.getRole());
            throw new InvalidRoleException("Invalid role: "+ user.getRole());
        }

        repo.save(existing);
        logger.info("User with Id: {} updated successfully", id);
        return mapper.map(existing, UserResponseDto.class);
    }

    @Override
    public String DeleteUser(Long id) {
        logger.info("Attempting to delete user with Id: {}",id);
        try{
            repo.deleteById(id);
            logger.info("Delete User with Id: {}", id);
            return "Deleted the User with id: "+ id+" Successfully";
        }
       catch(UserNotFoundException e){
            logger.error("Failed to delete user with ID: {} - user not found", id);
            throw new UserNotFoundException("The user is not created successfully..");
       }
    }



    @Override
    public String LoginUser(LoginDto user) {
        logger.info("Attempting to log in user with email: {}",user.getEmail());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        if(user==null){
            logger.warn("LoginDto is null");
            return "Login DTo mapping is null";
        }
        if (!user.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            logger.warn("Invalid email format: {}" , user.getEmail());
            return "Email should be a valid one";
        }
        if (user.getPassword() == null) {
            logger.warn("Password is null for user: {}", user.getEmail());
            return "Password should not be null";
        }
        if (user.getPassword().length() < 6) {
            logger.warn("Password too short for user: {}", user.getEmail());
            return "Password must be at least 8 characters";
        }

        String email = user.getEmail().trim();
        Optional<User> existingUser = repo.findByEmail(email);

        if (existingUser.isEmpty()) {
            logger.warn("User not found with email: {}", email);
            return "User Not found";
        }

        if (encoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            logger.info("User logged in successfully: {}", email);
            return "Login Successful";
        }
        logger.warn("Login failed due to incorrect credentials for user: {}", email);
        return "Failed to login: Incorrect credentials";
    }
}
