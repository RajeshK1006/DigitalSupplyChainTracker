package com.supplytracker.service;


import com.supplytracker.dto.AlertDto;
import java.util.List;


public interface AlertService {
	List<AlertDto> getAllAlerts();
	void resolveAlert(Long id);
}
