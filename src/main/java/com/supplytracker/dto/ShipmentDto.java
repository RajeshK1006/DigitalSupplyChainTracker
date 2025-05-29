package com.supplytracker.dto;

import java.time.LocalDateTime;

import com.supplytracker.entity.Item;
import com.supplytracker.entity.ShipmentStatus;
import com.supplytracker.entity.User;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data //creates get, set methods
@NoArgsConstructor //creates a default constructor
@AllArgsConstructor //creates a parameterized constructor

public class ShipmentDto {

	private Long id;
	
//	@NotNull(message = "Item Id is required") //Validates that itemId field is validated
	private Long itemId;
	
//	@NotBlank(message = "From Location is required") //Validates that the fromLocation field is not blank
	@Size(min = 3, max = 100, message = "From Location should be under 3 to 100 characters") //Ensures size is between specified min and max values
	private String fromLocation;
	
//	@NotBlank(message = "To Location is required") //Validates that the toLocation field is not blank
	@Size(min = 3, max = 100, message = "To Location should be under 3 to 100 characters") //Ensures size is between specified min and max values
	private String toLocation;
	
//	@NotNull(message = "Shipment status is required") //Validates that the expectedDelivery field is not null
	private LocalDateTime expectedDelivery;
	
//	@NotNull(message = "Status is required") //Validates that the currentStatus field is not null
	private String currentStatus;
	
//	@NotNull(message = "Name of Transporter is required") //Validates that the assignedTransporter field is not null
	private Long assignedTransporterId;
}