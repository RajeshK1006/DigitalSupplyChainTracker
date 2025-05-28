package com.supplytracker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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
		Shipment shipment = shipmentRepository.findById(dto.getShipmentId()).orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + dto.getShipmentId()));
		CheckpointLog log = CheckpointLog.builder().shipment(shipment).location(dto.getLocation()).status(dto.getStatus()).timestamp(LocalDateTime.now()).build();
		checkpointLogRepository.save(log);
		return "Checkpoint log saved successfully";
	}
	
	
	public List<CheckpointDTO> getLogForShipment(Long shipmentId) {
		List<CheckpointLog> logs = checkpointLogRepository.findByShipmentIdOrderByTimestampAsc(shipmentId);
		List<CheckpointDTO>dtoList = new ArrayList<>();
		
		for(CheckpointLog log : logs) {
			CheckpointDTO dto = new CheckpointDTO();
			dto.setShipmentId(log.getShipment().getId());
			dto.setLocation(log.getLocation());
			dto.setStatus(log.getStatus());
			dto.setTimestamp(log.getTimestamp());
			
			dtoList.add(dto);
		}
		return dtoList;
	}

}
