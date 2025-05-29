package com.supplytracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.Imp.CheckPointLogService;


public class CheckpointLogServiceTest {

    private CheckpointLogRepository checkpointRepo;
    private ShipmentRepository shipmentRepo;
    private ModelMapper modelMapper;
    private CheckPointLogService service;

    @BeforeEach
    public void setUp() {
        checkpointRepo = mock(CheckpointLogRepository.class);
        shipmentRepo = mock(ShipmentRepository.class);
        modelMapper = new ModelMapper();

        service = new CheckPointLogService();
        service.checkrepo = checkpointRepo;
        service.shiprepo = shipmentRepo;
        service.mapper = modelMapper;
    }

    @Test
    public void testAddCheckpoint_Success() {
        Long shipmentId = 101L;

        // Prepare input DTO
        CheckpointDto dto = new CheckpointDto(
                null,
                shipmentId,
                "Hyderabad Hub",
                "IN_TRANSIT",
                LocalDateTime.of(2025, 5, 29, 10, 30)
        );

        // Stub shipment repository
        Shipment shipment = new Shipment();
        shipment.setId(shipmentId);
        when(shipmentRepo.findById(shipmentId)).thenReturn(Optional.of(shipment));

        // Stub save operation
        CheckpointLog checkpointLog = modelMapper.map(dto, CheckpointLog.class);
        checkpointLog.setShipment(shipment);
        when(checkpointRepo.save(any(CheckpointLog.class))).thenReturn(checkpointLog);

        // Call service method
        CheckpointDto savedDto = service.addCheckpoint(dto);

        // Assertions
        assertNotNull(savedDto);
        assertEquals("Hyderabad Hub", savedDto.getLocation());
        assertEquals("IN_TRANSIT", savedDto.getStatus());

        // Verify interactions
        verify(shipmentRepo, times(1)).findById(shipmentId);
        verify(checkpointRepo, times(1)).save(any(CheckpointLog.class));
    }

    @Test
    public void testAddCheckpoint_ShipmentNotFound() {
        Long shipmentId = 999L;

        CheckpointDto dto = new CheckpointDto(
                null,
                shipmentId,
                "Missing Location",
                "DELAYED",
                LocalDateTime.now()
        );

        when(shipmentRepo.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.addCheckpoint(dto));

        verify(shipmentRepo, times(1)).findById(shipmentId);
        verify(checkpointRepo, never()).save(any());
    }
}
