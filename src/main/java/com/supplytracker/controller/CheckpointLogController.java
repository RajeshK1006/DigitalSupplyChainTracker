package com.supplytracker.controller;

import com.supplytracker.dto.CheckpointDto;
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
	public ResponseEntity<CheckpointDto>addCheckpoint(@Valid @RequestBody CheckpointDto request){
		CheckpointDto message = service.addCheckpoint(request);
		return ResponseEntity.ok(message);
	}
	
	@GetMapping("/shipment/{shipmentId}")

	public ResponseEntity<List<CheckpointDto>> getLogForShipment(@PathVariable Long shipmentId){
		List<CheckpointDto> logs = service.getCheckpointByShipment(shipmentId);
		return ResponseEntity.ok(logs);
	}
}
