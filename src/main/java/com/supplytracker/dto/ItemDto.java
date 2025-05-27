package com.supplytracker.dto;

import java.time.LocalDateTime;

import com.supplytracker.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data //creates get, set methods
@NoArgsConstructor //creates a default constructor
@AllArgsConstructor //creates a parameterized constructor
public class ItemDto {

	private Long id;
	
	@NotBlank(message = "Name is required") //Validation to enter a name if user enters a blank value
	@Size(min = 2, max = 100, message = "Size should be under 2 to 100 characters") //Validates that size is between a specified min and max value
	private String name;
	
	@NotBlank(message = "Category is required") //Validation to enter a category if user enters a blank value
	@Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters") //Validates that size is between a specified min and max value
	private String category;
	
	@NotNull(message = "Supplier Id is required") //Validation to enter a supplier id if user enters a blank value
	private Long supplierId;
	
	@PastOrPresent(message = "Datetime cannot be in the future") //Ensures date and time given are from past or present, but not from the future
	@NotNull(message = "Date Time is required") //Validation to enter a date and time if user enters a blank value
	private LocalDateTime datetime;
}
