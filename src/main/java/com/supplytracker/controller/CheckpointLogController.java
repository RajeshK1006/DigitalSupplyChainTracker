package com.supplytracker.controller;

import com.supplytracker.dto.CheckpointDTO;
import com.supplytracker.service.CheckpointLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor


public class CheckpointLogController {

	private final CheckpointLogService checkpointLogService;
	@PostMapping
	public ResponseEntity<String>addCheckpoint(@Valid @RequestBody CheckpointDTO request){
		String message = checkpointLogService.addCheckpoint(request);
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/shipment/{shipmentId}")
	public ResponseEntity<List<CheckpointDTO>> getLogs(@PathVariable Long shipmentId){
		List<CheckpointDTO> logs = checkpointLogService.getLogForShipment(shipmentId);
		return ResponseEntity.ok(logs);
	}
}
