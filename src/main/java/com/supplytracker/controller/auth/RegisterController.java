/**
 * 
 */
package com.supplytracker.controller.auth;

import com.supplytracker.dto.UserResponseDto;
import com.supplytracker.service.Imp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supplytracker.dto.LoginDto;
import com.supplytracker.dto.UserDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/auth")
public class RegisterController {

	@Autowired
	private UserService service;


	@PostMapping("/register")
	public UserResponseDto HandleRegistration(@Valid @RequestBody UserDto user){
		return service.createUser(user);
	}


	@PostMapping("/login")
	public String HandleLogin(@RequestBody LoginDto user){
		return service.LoginUser(user);
	}


	
	
	
	

}
