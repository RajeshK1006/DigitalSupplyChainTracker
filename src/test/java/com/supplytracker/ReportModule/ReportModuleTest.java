package com.supplytracker.ReportModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.Shipment;
import com.supplytracker.entity.ShipmentStatus;
import com.supplytracker.entity.Status;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.Imp.ReportService;


public class ReportModuleTest {

    private ReportService reportService;

    private ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
    private CheckpointLogRepository checkpointLogRepository = mock(CheckpointLogRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void setUp() {
        reportService = new ReportService();

        // Injecting mocks into private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(reportService, "shiprepo", shipmentRepository);
        ReflectionTestUtils.setField(reportService, "checkrepo", checkpointLogRepository);
        ReflectionTestUtils.setField(reportService, "itemrepo", itemRepository);
        ReflectionTestUtils.setField(reportService, "mapper", modelMapper);
    }

    @Test
    public void testGetDelayedShipments_ReturnsOnlyDelayedShipments() {
        Item item = new Item();
        item.setName("Laptop");

        Shipment delayedShipment = new Shipment();
        delayedShipment.setId(1L);
        delayedShipment.setItem(item);
        delayedShipment.setFromLocation("Delhi");
        delayedShipment.setToLocation("Mumbai");
        delayedShipment.setExpectedDelivery(LocalDateTime.now().plusDays(2));
        delayedShipment.setCurrentStatus(ShipmentStatus.DELAYED);

        Shipment onTimeShipment = new Shipment();
        onTimeShipment.setId(2L);
        onTimeShipment.setItem(item);
        onTimeShipment.setFromLocation("Pune");
        onTimeShipment.setToLocation("Kolkata");
        onTimeShipment.setExpectedDelivery(LocalDateTime.now().plusDays(1));
        onTimeShipment.setCurrentStatus(ShipmentStatus.IN_TRANSIT);

        when(shipmentRepository.findAll()).thenReturn(Arrays.asList(delayedShipment, onTimeShipment));

        List<ReportDelayedSupplyDto> delayedShipments = reportService.getDelayedShipments();

        assertEquals(1, delayedShipments.size());
        ReportDelayedSupplyDto dto = delayedShipments.get(0);
        assertEquals("Laptop", dto.getItemName());
        assertEquals("Delhi", dto.getFromLocation());
        assertEquals("Mumbai", dto.getToLocation());
        assertEquals("DELAYED", dto.getCurrentStatus());
        assertNotNull(dto.getNote());
        assertTrue(dto.getNote().contains("shipment is delayed"));
    }

    @Test
    public void testGetDeliveryPerformance_ReturnsCorrectDtos() {
        Item item = new Item();
        item.setName("Smartphone");

        Shipment shipment = new Shipment();
        shipment.setItem(item);

        CheckpointLog logDelivered = new CheckpointLog();
        logDelivered.setShipment(shipment);
        logDelivered.setLocation("Warehouse A");
        logDelivered.setStatus(Status.DELIVERED);  // Use your Status enum here

        CheckpointLog logInTransit = new CheckpointLog();
        logInTransit.setShipment(shipment);
        logInTransit.setLocation("Checkpoint B");
        logInTransit.setStatus(Status.IN_TRANSIT); // Use your Status enum here

        when(checkpointLogRepository.findAll()).thenReturn(Arrays.asList(logDelivered, logInTransit));

        List<ReportDeliveryPerformanceDto> performanceDtos = reportService.getDeliveryPerformance();

        assertEquals(2, performanceDtos.size());

        ReportDeliveryPerformanceDto dtoDelivered = performanceDtos.get(0);
        assertEquals("Smartphone", dtoDelivered.getItemName());
        assertEquals("Warehouse A", dtoDelivered.getLastKnownLocation());
        assertEquals("DELIVERED", dtoDelivered.getDeliveryStatus());
        assertEquals("Delivered Successfully", dtoDelivered.getPerformanceNote());

        ReportDeliveryPerformanceDto dtoInTransit = performanceDtos.get(1);
        assertEquals("Smartphone", dtoInTransit.getItemName());
        assertEquals("Checkpoint B", dtoInTransit.getLastKnownLocation());
        assertEquals("IN_TRANSIT", dtoInTransit.getDeliveryStatus());
        assertEquals("Shipment is on the way", dtoInTransit.getPerformanceNote());
    }

}

