package com.supplytracker.dto;

//DTO for ReportDailySupply which helps us to 

import lombok.Data;
import java.time.LocalDate;


@Data
public class ReportDelayedSupplyDto {
	
	private String itemName;
	private String fromLocation;
	private String toLocation;
	private String currentStatus;
	private LocalDate expectedDate;
	private String note;
	
}
