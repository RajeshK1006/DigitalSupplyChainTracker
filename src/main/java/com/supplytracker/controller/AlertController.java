package com.supplytracker.controller;

import com.supplytracker.dto.AlertDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.service.Imp.AlertService;
import com.supplytracker.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Slf4j
public class AlertController {

    private final AlertService service;
    private final UserRepository repo;

    // Utility method to authorize ADMINs only
    private void authorizeAdmin(String email) {
        log.info("Authorizing admin with email: {}", email);
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email is not found"));
        if (!"ADMIN".equalsIgnoreCase(user.getRole().toString())) {
            log.warn("Access denied for user role: {}", user.getRole());
            throw new InvalidRoleException("Only ADMINs are authorized to perform this action");
        }
        log.info("Authorization successful for ADMIN: {}", email);
    }

    @GetMapping
    public List<AlertDto> getAllAlerts() {
        return service.getAllAlerts();
    }

    @GetMapping("/{id}")
    public AlertDto getAlertById(@PathVariable Long id) {
        return service.getAlertbyId(id);
    }

    @PostMapping
    public AlertDto createAlert(@Valid @RequestBody AlertDto dto,
                                @RequestParam String email) {
        authorizeAdmin(email);
        return service.createAlert(dto);
    }

    @PutMapping("/{id}/resolve")
    public AlertDto updateAlert(@Valid @PathVariable Long id,
                                @RequestBody AlertDto dto,
                                @RequestParam String email) {
        authorizeAdmin(email);
        return service.updateAlert(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAlertById(@Valid @PathVariable Long id) {
        service.deleteAlert(id);
    }
}
