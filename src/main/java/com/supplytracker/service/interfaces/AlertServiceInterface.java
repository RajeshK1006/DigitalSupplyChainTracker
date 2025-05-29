package com.supplytracker.service.interfaces;

// Importing the Alert DTO class which is used to transfer alert data
import com.supplytracker.dto.AlertDto;

import java.util.List;


// This interface defines the contract for the Alert Service layer
// It contains the abstract methods which the implementing class should define
public interface AlertServiceInterface {

    public List<AlertDto> getAllAlerts();
    public AlertDto getAlertbyId(Long id);
    public AlertDto updateAlert(Long id, AlertDto dto);
    public AlertDto createAlert(AlertDto dto);
    public void deleteAlert(Long id);

}
