package com.supplytracker.service.interfaces;


import com.supplytracker.dto.ShipmentDto;

import java.util.List;

public interface ShipmentServiceInterface {

    ShipmentDto addShipment(ShipmentDto dto);

    ShipmentDto getShipmentById(Long id);

    List<ShipmentDto> getAllShipments();

    ShipmentDto updateShipment(Long id, ShipmentDto dto);

    void deleteShipment(Long id);
}
