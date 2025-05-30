package com.supplytracker.controller;

import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.service.Imp.AlertService;
import com.supplytracker.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final AlertService service;
    private final UserRepository repo;

    // Only allows ADMIN users to perform certain operations
    private void authorizeAdmin(String email) {
        logger.info("Checking if user is admin");
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email is not found"));
        if (!"ADMIN".equalsIgnoreCase(user.getRole().toString())) {
            logger.warn("User is not authorized");
            throw new InvalidRoleException("Only ADMINs are authorized to perform this action");
        }
        logger.info("User is authorized as admin");
    }

    // Returns all alerts
    @GetMapping
    public List<AlertDto> getAllAlerts() {
        logger.info("Fetching all alerts");
        return service.getAllAlerts();
    }

    // Returns a specific alert by its ID
    @GetMapping("/{id}")
    public AlertDto getAlertById(@PathVariable Long id) {
        logger.info("Fetching alert by ID");
        return service.getAlertbyId(id);
    }

    // Creates a new alert (Admin only)
    @PostMapping
    public AlertDto createAlert(@Valid @RequestBody AlertDto dto,
                                @RequestParam String email) {
        logger.info("Creating a new alert");
        authorizeAdmin(email);
        return service.createAlert(dto);
    }

    // Resolves or updates an existing alert (Admin only)
    @PutMapping("/{id}/resolve")
    public AlertDto updateAlert(@Valid @PathVariable Long id,
                                @RequestBody AlertDto dto,
                                @RequestParam String email) {
        logger.info("Updating alert");
        authorizeAdmin(email);
        return service.updateAlert(id, dto);
    }

    // Deletes an alert by ID
    @DeleteMapping("/{id}")
    public void deleteAlertById(@Valid @PathVariable Long id) {
        logger.info("Deleting alert");
        service.deleteAlert(id);
    }
}
