package com.supplytracker.service.interfaces;

import com.supplytracker.dto.AlertDto;

import java.util.List;

public interface AlertServiceInterface {

    public List<AlertDto> getAllAlerts();
    public AlertDto getAlertbyId(Long id);
    public AlertDto updateAlert(Long id, AlertDto dto);
    public AlertDto createAlert(AlertDto dto);
    public void deleteAlert(Long id);

}
