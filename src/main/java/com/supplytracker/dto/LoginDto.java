package com.supplytracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
public class LoginDto {

	@Email(message = "Email should be a valid one.")
	@NotNull(message = "Email should not be null")
	private String email;

	@NotNull(message = "Password must not be null")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	public LoginDto() {
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

