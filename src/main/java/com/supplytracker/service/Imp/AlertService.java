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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


@Service
public class AlertService implements AlertServiceInterface {

    @Autowired
    public AlertRepository alertrepo;

    @Autowired
    public ShipmentRepository shiprepo;

    @Autowired
    public ModelMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Override
    public List<AlertDto> getAllAlerts() {
        logger.info("Fetching all alerts");
        List<Alert> arr = alertrepo.findAll();
        List<AlertDto> res = new ArrayList<>();
        for(Alert a: arr){
            res.add(mapper.map(a, AlertDto.class));
        }
        logger.info("Total alerts fetched: {}", res.size());
        return res;
    }

    @Override
    public AlertDto getAlertbyId(Long id) {
        logger.info("Fetching alert with id: {}", id);
        Alert alert = alertrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alert not found with id: {}", id);
                    return new AlertNotFoundException("The Alert with this id not found!!!");
                });
        return mapper.map(alert, AlertDto.class);
    }

    @Override
    public AlertDto updateAlert(Long id, AlertDto dto) {
        logger.info("Updating alert with id: {}", id);
        Alert existing_Alert = alertrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alert not found with id: {}", id);
                    return new AlertNotFoundException("Alert with this id is not found");
                });

        // Update resolved status if changed
        if(dto.isResolved() != existing_Alert.isResolved()){
            existing_Alert.setResolved(dto.isResolved());
            logger.info("Updated alert resolved status to: {}", dto.isResolved());
        }
        // Update message if provided
        if(dto.getMessage() != null){
            existing_Alert.setMessage(dto.getMessage());
            logger.info("Updated alert message");
        }
        // Update shipment if provided
        if(dto.getShipmentId() != null){
            Shipment existing_shipment = shiprepo.findById(dto.getShipmentId())
                    .orElseThrow(() -> {
                        logger.error("Shipment not found with id: {}", dto.getShipmentId());
                        return new ShipmentNotFoundException("Shipment with this id is not found");
                    });
            existing_Alert.setShipment(existing_shipment);
            logger.info("Updated shipment association");
        }
        // Update alert type if provided
        if(dto.getType() != null){
            existing_Alert.setType(AlertType.valueOf(dto.getType()));
            logger.info("Updated alert type to: {}", dto.getType());
        }
        // Update creation date if provided
        if(dto.getCreatedOn() != null){
            existing_Alert.setCreatedOn(dto.getCreatedOn());
            logger.info("Updated alert creation date");
        }

        Alert updated = alertrepo.save(existing_Alert);
        logger.info("Alert updated successfully with id: {}", id);
        return mapper.map(updated, AlertDto.class);
    }

    @Override
    public AlertDto createAlert(AlertDto dto) {
        logger.info("Creating alert for shipment id: {}", dto.getShipmentId());
        Shipment shipment = shiprepo.findById(dto.getShipmentId())
                .orElseThrow(() -> {
                    logger.error("Shipment not found with id: {}", dto.getShipmentId());
                    return new ShipmentNotFoundException("Shipment with this id is not Found");
                });
        Alert alert = mapper.map(dto, Alert.class);
        alert.setShipment(shipment);
        alertrepo.save(alert);
        logger.info("Alert created successfully");
        return mapper.map(alert, AlertDto.class);
    }

    @Override
    public void deleteAlert(Long id) {
        logger.info("Deleting alert with id: {}", id);
        Alert alert = alertrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alert not found with id: {}", id);
                    return new AlertNotFoundException("Alert Not Found with this id");
                });
        alertrepo.delete(alert);
        logger.info("Alert deleted successfully with id: {}", id);
    }
}
