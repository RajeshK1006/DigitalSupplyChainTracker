package com.supplytracker.serviceImpl;


import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.Alert;
import com.supplytracker.repository.AlertRepository;
import com.supplytracker.service.AlertService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class AlertServiceImpl implements AlertService {
	
	@Autowired
	private AlertRepository alertRepo;
	
	@Override
	public List<AlertDto> getAllAlerts(){
		List<Alert> alertList = alertRepo.findAll();
		List<AlertDto> dtoList = new ArrayList<>();		
		
		for (Alert alert : alertList) {
			AlertDto dto = new AlertDto(
					alert.getId(),
					alert.getType(),
					alert.getMessage(),
					alert.isResolved(),
					alert.getCreatedOn(),
					alert.getShipment().getId()
			);
			dtoList.add(dto);
		}
		return dtoList;	
	}

	@Override
	public void resolveAlert(Long id) {
		Alert alert = alertRepo.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Alert with ID " + id + " Not Found"));
		alert.setResolved(true);
		alertRepo.save(alert);
	}
}
