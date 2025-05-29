package com.supplytracker.CheckpointModule;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.service.Imp.CheckPointLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CheckPointLogServiceIntegrationTest {

    @Autowired
    private CheckPointLogService service;

    @Test
    public void testAddAndFetchCheckpoint_forExistingShipment() {
        // 👇 Must match an existing shipment ID in your database
        Long shipmentId = 1L;

        // 👇 Must match an enum value in your Status.java
        CheckpointDto checkpoint = new CheckpointDto();
        checkpoint.setShipmentId(shipmentId);
        checkpoint.setLocation("Checkpoint Alpha");
        checkpoint.setStatus("IN_TRANSIT"); // Use exactly from enum
        checkpoint.setTimestamp(LocalDateTime.now());

        // Save checkpoint
        CheckpointDto saved = service.addCheckpoint(checkpoint);
        assertNotNull(saved);
        assertEquals("Checkpoint Alpha", saved.getLocation());

        // Fetch all checkpoints for this shipment
        List<CheckpointDto> logs = service.getCheckpointByShipment(shipmentId);
        assertFalse(logs.isEmpty());

        // Last log should be the one just added
        CheckpointDto latest = logs.get(logs.size() - 1);
        assertEquals("Checkpoint Alpha", latest.getLocation());
        assertEquals("IN_TRANSIT", latest.getStatus());
    }
}
