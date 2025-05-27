/**
 * 
 */
package com.supplytracker.controller.auth;

import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;
import com.supplytracker.entity.User;
import com.supplytracker.repository.UserRepository;

import jakarta.validation.Valid;
/**
 * 
 */

@RestController
@RequestMapping("api/auth")
public class RegisterController {
	
	
//	private final UserRepository userRepo;
//	private final ModelMapper mapper;
//	private final PasswordEncoder encoder;
//
//	@Autowired
//	public RegisterController(UserRepository userRepo, ModelMapper mapper, PasswordEncoder encode) {
//		this.userRepo = userRepo;
//		this.mapper = mapper;
//		this.encoder = encode;
//	}
//
//
//	@PostMapping("/register")
//	public String HandleRegister(@Valid @RequestBody UserDto user) {
//		User newuser = mapper.map(user, User.class);
//		if(newuser!=null) {
//			newuser.setPassword(encoder.encode(user.getPassword()));
//			userRepo.save(newuser);
//			return "User was Registered Sucessfully";
//		}
//		return "Failed to Register";
//	}
//
//
//
//	@PostMapping("/login")
//	public String HandleLogin(@Valid @RequestBody LoginDto user) {
//		User ExistingUser = userRepo.findByEmail(user.getEmail());
//		if(ExistingUser==null) {
//			return "User Not found";
//		}
//
//		if(encoder.matches(user.getPassword(),ExistingUser.getPassword())) {
//			return "Login SucessFull";
//		}
//
//		return "Failded to login";
//	}

	@Autowired
	private UserService service;


	@PostMapping("/register")
	public UserDto HandleRegistration( @Valid @RequestBody UserDto user){
		return service.createUser(user);
	}


	@PostMapping("/login")
	public String HandleLogin(LoginDto user){
		return service.LoginUser(user);
	}


	
	
	
	

}
