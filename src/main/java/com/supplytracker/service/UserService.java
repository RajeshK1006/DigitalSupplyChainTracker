package com.supplytracker.service;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.UserServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow(()-> new UserNotFoundException("The user with this id: "+ id +" not found"));
    }



    @Override
    public UserDto createUser(UserDto user) {
        User newuser = mapper.map(user, User.class);


           newuser.setPassword(encoder.encode(user.getPassword()));
           repo.save(newuser);
           return mapper.map(newuser, UserDto.class);

    }

    @Override
    public UserDto UpdateUser(Long id, UserDto user) {
        User existing = repo.findById(id).orElseThrow(()-> new UserNotFoundException("User with this id: "+id+" not found"));
        try{
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setPassword(encoder.encode(user.getPassword()));

            existing.setRole(Role.valueOf(user.getRole().toUpperCase()));
        }
        catch(IllegalArgumentException ex){
            throw new InvalidRoleException("Invalid role: "+ user.getRole());
        }

        repo.save(existing);
        return mapper.map(existing, UserDto.class);
    }

    @Override
    public String DeleteUser(Long id) {
        try{
            repo.deleteById(id);
            return "Deleted the User with id: "+ id+" Successfully";
        }
       catch(UserNotFoundException e){
            throw new UserNotFoundException("The user is not created successfully..");
       }
    }



    @Override
    public String LoginUser(LoginDto user) {
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        if(user==null){
            return "Login DTo mapping is null";
        }
     /*   if (user.getEmail().length()==0) {
            System.out.println(user.getEmail());
            return "Email should not be null";
        }*/
        if (!user.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            return "Email should be a valid one";
        }
        if (user.getPassword() == null) {
            return "Password should not be null";
        }
        if (user.getPassword().length() < 6) {
            return "Password must be at least 8 characters";
        }

        String email = user.getEmail().trim();
        User existingUser = repo.findByEmailIgnoreCase(email);

        if (existingUser == null) {
            return "User Not found";
        }

        if (encoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Login Successful";
        }

        return "Failed to login: Incorrect credentials";
    }

}
