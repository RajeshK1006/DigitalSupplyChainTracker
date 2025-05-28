package com.supplytracker.controller;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.service.interfaces.ItemServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

	@Autowired
	private ItemServiceInterface.ShipmentService shipmentService;

	@PostMapping
	public ResponseEntity<ShipmentDto> createShipment(@Valid @RequestBody ShipmentDto shipmentDto){
		ShipmentDto created = shipmentService.addShipment(shipmentDto);
		return ResponseEntity.ok(created);
	}

	@PutMapping("/{id}/assign")
	public ResponseEntity<ShipmentDto> assignTransporter(@PathVariable Long id,@Valid @RequestBody ShipmentDto shipmentDto){
		ShipmentDto updated = shipmentService.updateShipment(id,shipmentDto);
		return ResponseEntity.ok(updated);
	}

	@GetMapping
	public ResponseEntity<List<ShipmentDto>> getAllShipments(){
		return ResponseEntity.ok(shipmentService.getAllShipments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShipmentDto> getShipmentById(@PathVariable Long id){
		return ResponseEntity.ok(shipmentService.getShipmentById(id));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<ShipmentDto> updateShipmentStatus(@PathVariable Long id,@RequestBody ShipmentDto dto){
		ShipmentDto updated = shipmentService.updateShipment(id,dto);
		return ResponseEntity.ok(updated);
	}
}