package com.supplytracker.dto;

// Importing the AlertType enum to use the type of alert in this DTO
import com.supplytracker.entity.AlertType;
import java.time.LocalDateTime;

// Using Lombok annotations to automatically generate code like getters, setters, constructor, etc.
import lombok.*;


// This DTO class is used to transfer alert data between the frontend and backend
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDto {
	
	// Initialization of the objects of AlertDto
	public Long id;
	public AlertType type;
	public String message;
	public boolean resolved;
	public LocalDateTime createdOn;
	public Long shipmentId;

	
}