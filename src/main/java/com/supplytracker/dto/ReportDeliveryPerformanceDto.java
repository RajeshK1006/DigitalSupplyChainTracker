package com.supplytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReportDeliveryPerformanceDto {
	
	private String itemName;
	private String lastKnownLocation;
	private String deliveryStatus;
	private String performanceNote;

}
