package com.supplytracker.shipmentModule;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.entity.*;
import com.supplytracker.exception.*;
import com.supplytracker.repository.*;
import com.supplytracker.service.Imp.ShippmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShippmentServiceTest {

    @InjectMocks
    private ShippmentService shippmentService;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private Shipment shipment;
    private ShipmentDto shipmentDto;
    private User transporter;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        item = new Item();
        item.setId(1L);

        transporter = new User();
        transporter.setId(1L);
        transporter.setRole(Role.TRANSPORTER);

        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setItem(item);
        shipment.setFromLocation("A");
        shipment.setToLocation("B");
        shipment.setExpectedDelivery(LocalDateTime.now());
        shipment.setCurrentStatus(ShipmentStatus.IN_TRANSIT);
        shipment.setAssignedTransporter(transporter);

        shipmentDto = new ShipmentDto();
        shipmentDto.setId(1L);
        shipmentDto.setItemId(1L);
        shipmentDto.setFromLocation("A");
        shipmentDto.setToLocation("B");
        shipmentDto.setExpectedDelivery(LocalDateTime.now());
        shipmentDto.setCurrentStatus("IN_TRANSIT");
        shipmentDto.setAssignedTransporterId(1L);
    }

    @Test
    void addShipment_ShouldReturnSavedDto() {
        when(modelMapper.map(shipmentDto, Shipment.class)).thenReturn(shipment);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(transporter));
        when(shipmentRepository.save(any())).thenReturn(shipment);
        when(modelMapper.map(shipment, ShipmentDto.class)).thenReturn(shipmentDto);

        ShipmentDto result = shippmentService.addShipment(shipmentDto);

        assertNotNull(result);
        assertEquals("B", result.getToLocation());
        verify(shipmentRepository, times(1)).save(any());
    }


    @Test
    void addShipment_ShouldThrowIfUserNotTransporter() {
        transporter.setRole(Role.ADMIN);
        when(modelMapper.map(shipmentDto, Shipment.class)).thenReturn(shipment);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(transporter));

        assertThrows(IllegalArgumentException.class, () -> shippmentService.addShipment(shipmentDto));
    }

    @Test
    void getShipmentById_ShouldReturnShipmentDto() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(modelMapper.map(shipment, ShipmentDto.class)).thenReturn(shipmentDto);

        ShipmentDto result = shippmentService.getShipmentById(1L);

        assertNotNull(result);
        assertEquals("B", result.getToLocation());
    }

    @Test
    void getShipmentById_ShouldThrowIfNotFound() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class, () -> shippmentService.getShipmentById(1L));
    }

    @Test
    void getAllShipments_ShouldReturnShipmentDtoList() {
        when(shipmentRepository.findAll()).thenReturn(List.of(shipment));
        when(modelMapper.map(shipment, ShipmentDto.class)).thenReturn(shipmentDto);

        List<ShipmentDto> result = shippmentService.getAllShipments();

        assertEquals(1, result.size());
        assertEquals("B", result.get(0).getToLocation());
    }

    @Test
    void updateShipment_ShouldUpdateFields() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(transporter));
        when(shipmentRepository.save(any())).thenReturn(shipment);
        when(modelMapper.map(shipment, ShipmentDto.class)).thenReturn(shipmentDto);

        ShipmentDto result = shippmentService.updateShipment(1L, shipmentDto);

        assertEquals("B", result.getToLocation());
        verify(shipmentRepository).save(any());
    }

    @Test
    void updateShipment_ShouldThrowIfNotFound() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class, () -> shippmentService.updateShipment(1L, shipmentDto));
    }

    @Test
    void deleteShipment_ShouldDeleteSuccessfully() {
        doNothing().when(shipmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> shippmentService.deleteShipment(1L));
        verify(shipmentRepository).deleteById(1L);
    }

    @Test
    void deleteShipment_ShouldThrowIfRuntimeError() {
        doThrow(new RuntimeException()).when(shipmentRepository).deleteById(1L);

        assertThrows(ShipmentNotFoundException.class, () -> shippmentService.deleteShipment(1L));
    }
}