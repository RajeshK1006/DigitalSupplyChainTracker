package com.supplytracker.controller;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.service.Imp.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/delayed-shipments")
	public ResponseEntity<List<ReportDelayedSupplyDto>> getDelayedShipments(){
		List<ReportDelayedSupplyDto> delayedShipments = reportService.getDelayedShipments();
		return ResponseEntity.ok(delayedShipments);
	}
	
	@GetMapping("/delivery-performance")
	public ResponseEntity<List<ReportDeliveryPerformanceDto>> getDeliveryPerformance(){
		List<ReportDeliveryPerformanceDto> performanceLogs = reportService.getDeliveryPerformance();
		return ResponseEntity.ok(performanceLogs);
	}
}
