package com.supplytracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointDto {

	private Long id;

	@NotNull(message ="Shipment ID is required")
	private Long shipmentId;

	@NotNull(message ="Location is required")
	@Size(min = 2, max=100, message ="Location must be between 2 and 100 characters")
	private String location;

	@NotNull(message ="Status is required")
	private String status;
	@NotNull(message = "timestamp is require")
	private LocalDateTime timestamp;
	
}
