package com.supplytracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	
	
	@Email(message = "Email should be an Valid one.")
	@NotNull(message = "Email should be not null")
	private String email;
	
	@NotNull( message="Password must be not null")
	@Size(min=8, message="Password must be atleast 8 characters")
	private String password;

}
