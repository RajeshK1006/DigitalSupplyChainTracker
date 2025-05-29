package com.supplytracker.service.Imp;

import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.Alert;
import com.supplytracker.entity.AlertType;
import com.supplytracker.entity.Shipment;
import com.supplytracker.exception.AlertNotFoundException;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.repository.AlertRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.service.interfaces.AlertServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlertService implements AlertServiceInterface {

    @Autowired
    private AlertRepository alertrepo;

    @Autowired
    private ShipmentRepository shiprepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<AlertDto> getAllAlerts() {
        List<Alert>  arr = alertrepo.findAll();
        List<AlertDto> res = new ArrayList<>();
        for(Alert a: arr){
            res.add(mapper.map(a, AlertDto.class));
        }

        return res;
    }

    @Override
    public AlertDto getAlertbyId(Long id) {
        Alert alert = alertrepo.findById(id).orElseThrow(()-> new AlertNotFoundException("The Alert with this id not found!!!"));
        return mapper.map(alert, AlertDto.class);
    }

    @Override
    public AlertDto updateAlert(Long id, AlertDto dto) {

        Alert existing_Alert = alertrepo.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert with this id is not found"));
        if(dto.isResolved()!=existing_Alert.isResolved()){
            existing_Alert.setResolved(dto.isResolved());
        }
        if(dto.getMessage() != null){
            existing_Alert.setMessage(dto.getMessage());
        }

        if(dto.getShipmentId()!=null){
            Shipment existing_shipment = shiprepo.findById(dto.getShipmentId()).orElseThrow(()-> new ShipmentNotFoundException("Shipment with this id is not found"));
            existing_Alert.setShipment(existing_shipment);
        }
        if(dto.getType()!=null){
            existing_Alert.setType(AlertType.valueOf(dto.getType()));
        }
        if(dto.getCreatedOn()!=null){
            existing_Alert.setCreatedOn(dto.getCreatedOn());
        }

        Alert updated = alertrepo.save(existing_Alert);
        return mapper.map(updated, AlertDto.class);
    }

    @Override
    public AlertDto createAlert(AlertDto dto) {
        Shipment shipment = shiprepo.findById(dto.getShipmentId()).orElseThrow(()->new ShipmentNotFoundException("Shipment with this id is not Found"));
        Alert alert = mapper.map(dto, Alert.class);
        alert.setShipment(shipment);
        alertrepo.save(alert);
        return mapper.map(alert, AlertDto.class);

    }

    @Override
    public void deleteAlert(Long id) {
        Alert alert = alertrepo.findById(id).orElseThrow(()-> new AlertNotFoundException("Allert Not Found with this id"));
        alertrepo.delete(alert);
        return;
    }
}
