package com.supplytracker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.supplytracker.dto.CheckpointDTO;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.Data;
@Data
@Service
@RequiredArgsConstructor

public class CheckpointLogService {
	private final CheckpointLogRepository checkpointLogRepository;
	private final ShipmentRepository shipmentRepository;
	
	public String addCheckpoint(CheckpointDTO dto) {
		Shipment shipment = shipmentRepository.findById(dto.getShipmentId()).orElseThrow(() -> new RuntimeException("Shipment not foundwith ID: " + dto.getShipmentId()));
		CheckpointLog log = CheckpointLog.builder().shipment(shipment).location(dto.getLocation()).status(dto.getStatus()).timestamp(LocalDateTime.now()).build();
		checkpointLogRepository.save(log);
		return "Checkpoint log saved successfully";
	}
	public List<CheckpointDTO> getLogForShipment(Long shipmentId) {
		List<CheckpointLog> logs = checkpointLogRepository.findByShipmentIdOrderByTimestampAsc(shipmentId);
		return logs.stream()
				.map(log -> new CheckpointDTO(
						log.getShipment().getId(),
				        log.getLocation(),
				        log.getStatus(),
				        log.getTimestamp()
			     )).collect(Collectors.toList());
	}

}
