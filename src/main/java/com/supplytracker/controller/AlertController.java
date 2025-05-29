package com.supplytracker.controller;


import com.supplytracker.dto.AlertDto;
import com.supplytracker.service.Imp.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// This controller class shows REST API endpoints for managing alerts
@RestController
@RequestMapping("/api/alerts")
public class AlertController {
	
    // Logger for monitoring API hits
	private static final Logger logger = LoggerFactory.getLogger(AlertController.class);


    @Autowired
    private AlertService service;

    
    // Get all alerts
    @GetMapping
    public List<AlertDto> getAllAlerts(){
        return service.getAllAlerts();
    }

    
    // Get a single alert by ID
    @GetMapping("/{id}")
    public AlertDto getAlertById(@PathVariable Long id){
    	logger.info("GET /api/alerts/{} called", id);
        return service.getAlertbyId(id);
    }

    
    // Create a new alert
    @PostMapping
    public AlertDto createAlert(@Valid @RequestBody AlertDto dto){
        return service.createAlert(dto);
    }

    
    // Update an alert by ID
    @PutMapping("/{id}/resolve")
    public AlertDto updateAlert(@Valid @PathVariable Long id, @RequestBody AlertDto dto){
        return service.updateAlert(id,dto);
    }

    
    // Delete an alert by ID
    @DeleteMapping("/{id}")
    public void deleteAlertById(@Valid @PathVariable Long id){
    	logger.warn("Deleting alert with ID: {}", id);
        service.deleteAlert(id);
        return;
    }


}
