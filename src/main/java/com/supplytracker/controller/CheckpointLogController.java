package com.supplytracker.controller;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.CheckPointLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
public class CheckpointLogController {

	private static final Logger logger = LoggerFactory.getLogger(CheckpointLogController.class);

	@Autowired
	private final CheckPointLogService service;

	@Autowired
	private final UserRepository repo;

	// Allows only MANAGER or TRANSPORTER to proceed
	private void authorizeManagerOrTransporter(String email) {
		logger.info("Authorizing user role check");

		User user = repo.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("User not found during authorization");
					return new UserNotFoundException("User with this email is not found");
				});

		if (!("MANAGER".equalsIgnoreCase(String.valueOf(user.getRole()))
				|| "TRANSPORTER".equalsIgnoreCase(String.valueOf(user.getRole())))) {
			logger.warn("User is not authorized to access this endpoint");
			throw new InvalidRoleException("Only MANAGERs and TRANSPORTERs are authorized to perform this action");
		}

		logger.info("User is authorized for checkpoint operation");
	}

	// Adds a checkpoint log for a shipment
	@PostMapping
	public ResponseEntity<CheckpointDto> addCheckpoint(@Valid @RequestBody CheckpointDto request,
													   @RequestParam String email) {
		authorizeManagerOrTransporter(email);
		logger.info("Checkpoint addition request received");
		CheckpointDto message = service.addCheckpoint(request);
		return ResponseEntity.ok(message);
	}

	// Fetches all checkpoints for a given shipment
	@GetMapping("/shipment/{shipmentId}")
	public ResponseEntity<List<CheckpointDto>> getLogForShipment(@PathVariable Long shipmentId,
																 @RequestParam String email) {
		authorizeManagerOrTransporter(email);
		logger.info("Fetching checkpoints for shipment");
		List<CheckpointDto> logs = service.getCheckpointByShipment(shipmentId);
		return ResponseEntity.ok(logs);
	}
}
