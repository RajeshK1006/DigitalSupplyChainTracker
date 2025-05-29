package com.supplytracker.service.Imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import com.supplytracker.entity.Status;
import com.supplytracker.exception.ShipmentNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.CheckpointDTO;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;



@Service
public class CheckPointLogService {

	@Autowired
	private CheckpointLogRepository checkrepo;

	@Autowired
	private ShipmentRepository shiprepo;

	@Autowired
	private ModelMapper mapper;


	
	public CheckpointDTO addCheckpoint(CheckpointDTO dto) {

		Shipment shipment = shiprepo.findById(dto.getShipmentId()).orElseThrow(()-> new ShipmentNotFoundException("The Shipment with this id is not Found"));
		CheckpointLog chpnt = mapper.map(dto,CheckpointLog.class);
		try{
			chpnt.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
		}
		catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Invalid Status value: "+ dto.getStatus());
		}

		chpnt.setShipment(shipment);
		checkrepo.save(chpnt);
		return mapper.map(chpnt,CheckpointDTO.class);


	}
	
	
	public List<CheckpointDTO> getCheckpointByShipment(Long shipmentId) {

		Shipment shipment = shiprepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("The shipment is not with this id"));
		List<CheckpointLog> ans = checkrepo.findByShipmentIdOrderByTimestampAsc(shipmentId);
		List<CheckpointDTO> result = new ArrayList<>();
		for (CheckpointLog ch : ans) {
			result.add(mapper.map(ch, CheckpointDTO.class));
		}

		return result;

	}

}
