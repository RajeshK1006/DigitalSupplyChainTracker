package com.supplytracker.controller;


import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.Alert;
import com.supplytracker.exception.AlertNotFoundException;
import com.supplytracker.service.Imp.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService service;

    @GetMapping
    public List<AlertDto> getAllAlerts(){
        return service.getAllAlerts();
    }

    @GetMapping("/{id}")
    public AlertDto getAlertById(@PathVariable Long id){
        return service.getAlertbyId(id);
    }

    @PostMapping
    public AlertDto createAlert(@Valid @RequestBody AlertDto dto){
        return service.createAlert(dto);
    }

    @PutMapping("/{id}/resolve")
    public AlertDto updateAlert(@Valid @PathVariable Long id, @RequestBody AlertDto dto){
        return service.updateAlert(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAlertById(@Valid @PathVariable Long id){
        service.deleteAlert(id);
        return;
    }

//
//    public AlertDto updateAlert(Long id, AlertDto dto) {
//        Alert existingAlert = alertrepo.findById(id)
//                .orElseThrow(() -> new AlertNotFoundException("Alert with this id is not found"));
//
//        // Only update the 'resolved' status
//        existingAlert.setResolved(dto.isResolved());
//
//        Alert updated = alertrepo.save(existingAlert);
//        return mapper.map(updated, AlertDto.class);
//    }



}
