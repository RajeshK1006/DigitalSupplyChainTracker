package com.supplytracker.controller;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.service.Imp.CheckPointLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.*;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
public class CheckpointLogController {
	private static final Logger logger = LoggerFactory.getLogger(CheckpointLogController.class);


	@Autowired
	private final CheckPointLogService service;


	@PostMapping
	public ResponseEntity<CheckpointDto>addCheckpoint(@Valid @RequestBody CheckpointDto request){
		logger.info("Received request to add checkpoint: {}", request);
		CheckpointDto message = service.addCheckpoint(request);
		logger.info("Checkpoint added successfully for shipmentId {}" , request.getShipmentId());
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/shipment/{shipmentId}")

	public ResponseEntity<List<CheckpointDto>> getLogForShipment(@PathVariable Long shipmentId){
		logger.info("Fetching checkpoint logs for shipmentId {}", shipmentId);
		List<CheckpointDto> logs = service.getCheckpointByShipment(shipmentId);
		logger.info("Retrieved {} logs for shipmentId {}", logs.size(), shipmentId);
		return ResponseEntity.ok(logs);
	}
}
