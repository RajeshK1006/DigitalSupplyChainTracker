package com.supplytracker.AlertModule;

import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.Alert;
import com.supplytracker.entity.AlertType;
import com.supplytracker.entity.Shipment;
import com.supplytracker.exception.AlertNotFoundException;
import com.supplytracker.repository.AlertRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.Imp.AlertService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


// This class contains unit tests for the AlertService methods using JUnit 5 and Mockito
public class AlertServiceTest {

    private AlertRepository alertRepo;
    private ShipmentRepository shipmentRepo;
    private ModelMapper modelMapper;
    private AlertService service;

    // This method runs before each test and initializes the mock dependencies and service object
    @BeforeEach
    public void setup() {
        alertRepo = mock(AlertRepository.class);
        shipmentRepo = mock(ShipmentRepository.class);
        modelMapper = new ModelMapper();
        service = new AlertService();
        service.alertrepo = alertRepo;
        service.shiprepo = shipmentRepo;
        service.mapper = modelMapper;
    }

    // Test case - 1: Verifies that getAllAlerts() returns the correct list of alerts
    @Test
    public void testGetAllAlerts() {
        Alert alert = new Alert(1L, new Shipment(), AlertType.DELAY, "Delayed", LocalDateTime.now(), false);
        when(alertRepo.findAll()).thenReturn(List.of(alert));
        List<AlertDto> result = service.getAllAlerts();
        assertEquals(1, result.size());
        assertEquals(AlertType.DELAY, result.get(0).getType());
    }

    // Test case - 2: Verifies that getAlertById() returns the alert when it exists
    @Test
    public void testGetAlertById_Found() {
        Alert alert = new Alert(1L, new Shipment(), AlertType.DAMAGE, "Damaged", LocalDateTime.now(), true);
        when(alertRepo.findById(1L)).thenReturn(Optional.of(alert));
        AlertDto dto = service.getAlertbyId(1L);
        assertEquals(AlertType.DAMAGE, dto.getType());
    }

    // Test case - 3: Verifies that getAlertById() throws an exception when alert is not found
    @Test
    public void testGetAlertById_NotFound() {
        when(alertRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(AlertNotFoundException.class, () -> service.getAlertbyId(99L));
    }

    // Test case - 4: Verifies that deleteAlert() calls the repository's delete method
    @Test
    public void testDeleteAlert() {
        Alert alert = new Alert(1L, new Shipment(), AlertType.DELAY, "Delayed", LocalDateTime.now(), false);
        when(alertRepo.findById(1L)).thenReturn(Optional.of(alert));
        service.deleteAlert(1L);
        verify(alertRepo, times(1)).delete(alert);
    }
}
