package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.entity.Status;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;


@Service
public class CheckPointLogService {

	@Autowired
	public CheckpointLogRepository checkrepo;

	@Autowired
	public ShipmentRepository shiprepo;

	@Autowired
	public ModelMapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CheckPointLogService.class);

	
	public CheckpointDto addCheckpoint(CheckpointDto dto) {
		logger.info("Received request to add checkpoint for shipment ID: {}", dto.getShipmentId());

		Shipment shipment = shiprepo.findById(dto.getShipmentId()).orElseThrow(()-> {
			logger.warn("Shipment with ID {} not found", dto.getShipmentId());
			return new ShipmentNotFoundException("The Shipment with this id is not Found");
		});
		CheckpointLog chpnt = mapper.map(dto,CheckpointLog.class);
		try{
			chpnt.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
		}
		catch(IllegalArgumentException ex){
			logger.error("Invalid status '{}' provided in checkpoint DTO", dto.getStatus());
			throw new IllegalArgumentException("Invalid Status value: "+ dto.getStatus());
		}

		chpnt.setShipment(shipment);
		checkrepo.save(chpnt);
		logger.info("Checkpoint saved successfully at location '{}' for shipment ID {}",
				     chpnt.getLocation(), dto.getShipmentId());
		return mapper.map(chpnt,  CheckpointDto.class);
		


	}
	
	
	public List<CheckpointDto> getCheckpointByShipment(Long shipmentId) {
		logger.info("Feeding checkpoints for shipment ID: {} ", shipmentId);

		Shipment shipment = shiprepo.findById(shipmentId).orElseThrow(() -> { 
			logger.warn("Shipment with ID {} not found", shipmentId);
			return new ShipmentNotFoundException("The shipment is not with this id");
		});
		List<CheckpointLog> ans = checkrepo.findByShipmentIdOrderByTimestampAsc(shipmentId);
		logger.info("Found {} checkpoint(s) for shipment ID: {}", ans.size(), shipmentId);
		List<CheckpointDto> result = new ArrayList<>();
		for (CheckpointLog ch : ans) {
			result.add(mapper.map(ch, CheckpointDto.class));
		}

		return result;

	}


	public List<CheckpointDto> getLogForShipment1(Long shipmentId) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<CheckpointDto> getLogForShipment(Long shipmentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
