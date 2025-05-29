package com.supplytracker.service.interfaces;

import java.util.List;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;

public interface ReportServiceInterface {
	
	List<ReportDelayedSupplyDto> getDelayedShipments();
	List<ReportDeliveryPerformanceDto> getDeliveryPerformance();
}
