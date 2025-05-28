package com.supplytracker.controller;


import com.supplytracker.dto.AlertDto;
import com.supplytracker.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/alerts")
public class AlertController {
	
	private final AlertService alertService;
	
	@Autowired
	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}
	
	@GetMapping
	public ResponseEntity<List<AlertDto>> getAllAlerts() {
		List<AlertDto> alerts = alertService.getAllAlerts();
		return ResponseEntity.ok(alerts);
	}
	
	@PutMapping("/{id}/resolve")
	public ResponseEntity<String> resolveAlert(@PathVariable Long id) {
		alertService.resolveAlert(id);
		return ResponseEntity.ok("Alert with ID " + id + " has been resolved.");
	} 
}
