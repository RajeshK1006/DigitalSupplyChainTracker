package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.entity.CheckpointLog;
import com.supplytracker.entity.Shipment;
import com.supplytracker.repository.CheckpointLogRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.interfaces.ReportServiceInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReportService implements ReportServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	private ShipmentRepository shiprepo;

	@Autowired
	private CheckpointLogRepository checkrepo;

	@Autowired
	private ModelMapper mapper;

	// Get list of delayed shipments with a note
	@Override
	public List<ReportDelayedSupplyDto> getDelayedShipments() {
		logger.info("Fetching delayed shipments");

		List<Shipment> shipments = shiprepo.findAll();
		List<Shipment> filtered = new ArrayList<>();
		for (Shipment sh : shipments) {
			if ("DELAYED".equalsIgnoreCase(String.valueOf(sh.getCurrentStatus()))) {
				filtered.add(sh);
			}
		}

		List<ReportDelayedSupplyDto> result = new ArrayList<>();
		for (Shipment sh : filtered) {
			ReportDelayedSupplyDto dto = mapper.map(sh, ReportDelayedSupplyDto.class);
			dto.setNote("Kindly understand that the shipment is delayed due to legit reasons");
			result.add(dto);
		}
		logger.info("Found {} delayed shipments", result.size());
		return result;
	}

	// Get delivery performance report based on checkpoint logs
	@Override
	public List<ReportDeliveryPerformanceDto> getDeliveryPerformance() {
		logger.info("Fetching delivery performance data");

		List<ReportDeliveryPerformanceDto> result = new ArrayList<>();
		List<CheckpointLog> logs = checkrepo.findAll();

		for (CheckpointLog log : logs) {
			ReportDeliveryPerformanceDto dto = new ReportDeliveryPerformanceDto();
			dto.setItemName(log.getShipment().getItem().getName());
			dto.setLastKnownLocation(log.getLocation());
			dto.setDeliveryStatus(log.getStatus().toString());

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
					break;
			}
			result.add(dto);
		}
		logger.info("Generated delivery performance report with {} entries", result.size());
		return result;
	}

}
