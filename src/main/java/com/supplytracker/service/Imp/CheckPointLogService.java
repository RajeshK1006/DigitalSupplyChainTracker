package com.supplytracker.service.Imp;

import java.util.List;
import java.util.ArrayList;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.entity.Status;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckPointLogService {

	private static final Logger logger = LoggerFactory.getLogger(CheckPointLogService.class);

	@Autowired
    public CheckpointLogRepository checkrepo;

	@Autowired
    public ShipmentRepository shiprepo;

	@Autowired
    public ModelMapper mapper;

	// Add a new checkpoint to a shipment
	public CheckpointDto addCheckpoint(CheckpointDto dto) {
		logger.info("Adding checkpoint for shipment ID: {}", dto.getShipmentId());

		Shipment shipment = shiprepo.findById(dto.getShipmentId())
				.orElseThrow(() -> {
					logger.error("Shipment not found with ID: {}", dto.getShipmentId());
					return new ShipmentNotFoundException("Shipment not found");
				});

		CheckpointLog chpnt = mapper.map(dto, CheckpointLog.class);

		try {
			chpnt.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
		} catch (IllegalArgumentException ex) {
			logger.error("Invalid status: {}", dto.getStatus());
			throw new IllegalArgumentException("Invalid status: " + dto.getStatus());
		}

		chpnt.setShipment(shipment);
		checkrepo.save(chpnt);
		logger.info("Checkpoint saved with ID: {}", chpnt.getId());

		return mapper.map(chpnt, CheckpointDto.class);
	}

	// Get all checkpoints for a shipment ordered by timestamp
	public List<CheckpointDto> getCheckpointByShipment(Long shipmentId) {
		logger.info("Getting checkpoints for shipment ID: {}", shipmentId);

		Shipment shipment = shiprepo.findById(shipmentId)
				.orElseThrow(() -> {
					logger.error("Shipment not found with ID: {}", shipmentId);
					return new ShipmentNotFoundException("Shipment not found");
				});

		List<CheckpointLog> checkpoints = checkrepo.findByShipmentIdOrderByTimestampAsc(shipmentId);
		List<CheckpointDto> result = new ArrayList<>();

		for (CheckpointLog ch : checkpoints) {
			result.add(mapper.map(ch, CheckpointDto.class));
		}

		logger.info("Found {} checkpoints", result.size());
		return result;
	}
}
