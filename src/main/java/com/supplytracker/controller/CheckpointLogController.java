package com.supplytracker.controller;

import com.supplytracker.dto.CheckpointDTO;
import com.supplytracker.service.Imp.CheckPointLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/checkpoints")
@RequiredArgsConstructor
public class CheckpointLogController {


	@Autowired
	private final CheckPointLogService service;


	@PostMapping
	public ResponseEntity<CheckpointDTO>addCheckpoint(@Valid @RequestBody CheckpointDTO request){
		CheckpointDTO message = service.addCheckpoint(request);
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/shipment/{shipmentId}")

	public ResponseEntity<List<CheckpointDTO>> getLogForShipment(@PathVariable Long shipmentId){
		List<CheckpointDTO> logs = service.getCheckpointByShipment(shipmentId);
		return ResponseEntity.ok(logs);
	}
}
