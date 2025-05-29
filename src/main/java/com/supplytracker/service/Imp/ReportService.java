package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.Shipment;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.interfaces.ReportServiceInterface;

@Service
public class ReportService  implements ReportServiceInterface{
	
	@Autowired
	private  ShipmentRepository shiprepo;
	@Autowired
	private  CheckpointLogRepository checkrepo;
	
	@Autowired
	private ItemRepository itemrepo;
	
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public List<ReportDelayedSupplyDto> getDelayedShipments(){

		
		List<Shipment> shipments = shiprepo.findAll();
		List<Shipment> filtered = new ArrayList<>();
		for(Shipment sh : shipments) {
			if(String.valueOf(sh.getCurrentStatus()).toUpperCase().equals("DELAYED")) {
				filtered.add(sh);
			}
		}
		
		List<ReportDelayedSupplyDto> result = new ArrayList<>();
		for(Shipment sh: filtered) {
			ReportDelayedSupplyDto dto = mapper.map(sh, ReportDelayedSupplyDto.class);
			dto.setNote("Kindly understand that the shipment is delayed due to legit reasons");
			result.add(dto);
		}
		
		return result;
	}
	
	@Override
	public List<ReportDeliveryPerformanceDto> getDeliveryPerformance(){
		List<ReportDeliveryPerformanceDto> result = new ArrayList<>();
		List<CheckpointLog> logs = checkrepo.findAll();
		
		for(CheckpointLog log:logs) {
			ReportDeliveryPerformanceDto dto = new ReportDeliveryPerformanceDto();
			dto.setItemName(log.getShipment().getItem().getName());
			dto.setLastKnownLocation(log.getLocation());
			dto.setDeliveryStatus(log.getStatus().toString());
			
			
			
//			ReportDeliveryPerformanceDto dto = new ReportDeliveryPerformanceDto();
//			dto.setItemName(item.getName());
//			dto.setDeliveryStatus(String.valueOf(log.getStatus()).toUpperCase());
//			dto.setLastKnownLocation(log.getLocation());
//			
			
			switch (log.getStatus()) {
				case DELIVERED:
					dto.setPerformanceNote("Delivered Successfully");
					break;
				case IN_TRANSIT:
					dto.setPerformanceNote("Shipment is on the way");
					break;
				case RECIEVED:
					dto.setPerformanceNote("Shipment Received at Checkpoint");
					break;
				case DAMAGED:
					dto.setPerformanceNote("Alert: Shipment Damaged");
					break;
				default:
					dto.setPerformanceNote("Status Unknown");
			}
			result.add(dto);
		}
		return result;
	}

}