package com.supplytracker.controller;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.interfaces.ItemServiceInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@Slf4j
public class ShipmentController {

	@Autowired
	private ItemServiceInterface.ShipmentService shipmentService;

	@Autowired
	private UserRepository repo;

	private void authorizeManager(String email) {
		log.info("Authorizing user with email: {}", email);
		User user = repo.findByEmail(email)
				.orElseThrow(() -> {
					log.error("User not found with email: {}", email);
					return new UserNotFoundException("User with this email is not found");
				});

		if (!"MANAGER".equalsIgnoreCase(String.valueOf(user.getRole()))) {
			log.warn("Unauthorized access attempt by user with email: {}", email);
			throw new InvalidRoleException("Only MANAGERs are authorized to perform this action");
		}
		log.info("Authorization successful for user: {}", email);
	}

	@PostMapping
	public ResponseEntity<ShipmentDto> createShipment(@Valid @RequestBody ShipmentDto shipmentDto,
													  @RequestParam String email) {
		log.info("Creating shipment by user: {}", email);
		authorizeManager(email);
		ShipmentDto created = shipmentService.addShipment(shipmentDto);
		log.info("Shipment created successfully with ID: {}", created.getId());
		return ResponseEntity.ok(created);
	}

	@PutMapping("/{id}/assign")
	public ResponseEntity<ShipmentDto> assignTransporter(@PathVariable Long id,
														 @Valid @RequestBody ShipmentDto shipmentDto,
														 @RequestParam String email) {
		log.info("Assigning transporter to shipment ID: {} by user: {}", id, email);
		authorizeManager(email);
		ShipmentDto updated = shipmentService.updateShipment(id, shipmentDto);
		log.info("Shipment updated with transporter assignment for ID: {}", id);
		return ResponseEntity.ok(updated);
	}

	@GetMapping
	public ResponseEntity<List<ShipmentDto>> getAllShipments(@RequestParam String email) {
		log.info("Fetching all shipments");
		authorizeManager(email);
		return ResponseEntity.ok(shipmentService.getAllShipments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShipmentDto> getShipmentById(@RequestParam String email,@PathVariable Long id) {
		log.info("Fetching shipment by ID: {}", id);
		authorizeManager(email);
		return ResponseEntity.ok(shipmentService.getShipmentById(id));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<ShipmentDto> updateShipmentStatus(@PathVariable Long id,
															@RequestBody ShipmentDto dto,
															@RequestParam String email) {
		log.info("Updating shipment status for ID: {} by user: {}", id, email);
		authorizeManager(email);
		ShipmentDto updated = shipmentService.updateShipment(id, dto);
		log.info("Shipment status updated for ID: {}", id);
		return ResponseEntity.ok(updated);
	}
}
