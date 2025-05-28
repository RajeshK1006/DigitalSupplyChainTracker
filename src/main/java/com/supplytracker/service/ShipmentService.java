package com.supplytracker.service;



import java.util.List;

import com.supplytracker.dto.ShipmentDto;

public interface ShipmentService {
    ShipmentDto addShipment(ShipmentDto shipmentdto);
    ShipmentDto getShipmentById(Long id);
    List<ShipmentDto> getAllShipments();
    ShipmentDto updateShipment(Long id, ShipmentDto shipmentDto);
    void deleteShipment(Long id);
}