package com.supplytracker.dto;

/*
 *This AlertDto is used to send the Alert data from Backend to Frontend
 */
import com.supplytracker.entity.AlertType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AlertDto {
	
	//Initialization of the objects of AlertDto
	private Long id;
	private AlertType type;
	private String message;
	private boolean resolved;
	private LocalDateTime createdOn;
	private Long shipmentId;

	
}