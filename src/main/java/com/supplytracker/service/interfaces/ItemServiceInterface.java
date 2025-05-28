package com.supplytracker.service.interfaces;

import java.util.List;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.dto.ShipmentDto;

public interface ItemServiceInterface {
	ItemDto addItems(ItemDto itemDto);
	ItemDto updateItem(Long id, ItemDto itemDto);
	List<ItemDto> getAllItems();
	ItemDto getById(Long id);
	void deleteItem(Long id);

	interface ShipmentService {
		ShipmentDto addShipment(ShipmentDto shipmentdto);
		ShipmentDto getShipmentById(Long id);
		List<ShipmentDto> getAllShipments();
		ShipmentDto updateShipment(Long id, ShipmentDto shipmentDto);
		void deleteShipment(Long id);
	}
}
