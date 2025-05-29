package com.supplytracker.service.Imp;

import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.Alert;
import com.supplytracker.entity.AlertType;
import com.supplytracker.entity.Shipment;
import com.supplytracker.exception.AlertNotFoundException;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.repository.AlertRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.interfaces.AlertServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// This service class contains the business logic for the Alert module
@Service
@Data
public class AlertService implements AlertServiceInterface {
	
    // Logger for logging important actions and errors
	private static final Logger logger = LoggerFactory.getLogger(AlertService.class);


    @Autowired
	public AlertRepository alertrepo;

    @Autowired
    public ShipmentRepository shiprepo;

    @Autowired
    public ModelMapper mapper;

    
    // Fetch all alerts from the database and return as DTOs
    @Override
    public List<AlertDto> getAllAlerts() {
        List<Alert>  arr = alertrepo.findAll();
        List<AlertDto> res = new ArrayList<>();
        for(Alert a: arr){
            res.add(mapper.map(a, AlertDto.class));
        }

        return res;
    }

    
    // Fetch a specific alert by ID
    @Override
    public AlertDto getAlertbyId(Long id) {
        Alert alert = alertrepo.findById(id).orElseThrow(()-> new AlertNotFoundException("The Alert with this id not found!!!"));
        return mapper.map(alert, AlertDto.class);
    }

    
    // Update an existing alert based on the provided DTO data
    @Override
    public AlertDto updateAlert(Long id, AlertDto dto) {

        Alert existing_Alert = alertrepo.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert with this id is not found"));
        if(dto.isResolved()!=existing_Alert.isResolved()){
            existing_Alert.setResolved(dto.isResolved());
        }
        if(dto.getMessage() != null){
            existing_Alert.setMessage(dto.getMessage());
        }

        if(dto.getShipmentId()!=null){
            Shipment existing_shipment = shiprepo.findById(dto.getShipmentId()).orElseThrow(()-> new ShipmentNotFoundException("Shipment with this id is not found"));
            existing_Alert.setShipment(existing_shipment);
        }
        if(dto.getType()!=null){
            existing_Alert.setType(dto.getType());
        }
        if(dto.getCreatedOn()!=null){
            existing_Alert.setCreatedOn(dto.getCreatedOn());
        }

        Alert updated = alertrepo.save(existing_Alert);
        return mapper.map(updated, AlertDto.class);
    }

    
    // Create and save a new alert
    @Override
    public AlertDto createAlert(AlertDto dto) {
    	logger.info("Creating alert for shipmentId: {}", dto.getShipmentId());
        Shipment shipment = shiprepo.findById(dto.getShipmentId())
        		.orElseThrow(()-> {
        			logger.error("Shipment with ID {} not found", dto.getShipmentId());
        			return new ShipmentNotFoundException("Shipment with this id is not Found");
        		});
        Alert alert = mapper.map(dto, Alert.class);
        alert.setShipment(shipment);
        alertrepo.save(alert);
        logger.info("Alert created with ID: {}", alert.getId());
        return mapper.map(alert, AlertDto.class);

    }

    
    // Delete an alert by ID
    @Override
    public void deleteAlert(Long id) {
        Alert alert = alertrepo.findById(id).orElseThrow(()-> new AlertNotFoundException("Allert Not Found with this id"));
        alertrepo.delete(alert);
        return;
    }
}
