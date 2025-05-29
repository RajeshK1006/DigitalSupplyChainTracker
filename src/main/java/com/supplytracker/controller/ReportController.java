package com.supplytracker.controller;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserRepository userRepository;

	private void authorizeAdminOrManager(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User with this email is not found"));

		String role = String.valueOf(user.getRole()).toUpperCase();
		if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
			throw new InvalidRoleException("Only ADMINs and MANAGERs are authorized to access this report");
		}
	}

	@GetMapping("/delayed-shipments")
	public ResponseEntity<List<ReportDelayedSupplyDto>> getDelayedShipments() {
		List<ReportDelayedSupplyDto> delayedShipments = reportService.getDelayedShipments();
		return ResponseEntity.ok(delayedShipments);
	}

	@GetMapping("/delivery-performance")
	public ResponseEntity<List<ReportDeliveryPerformanceDto>> getDeliveryPerformance(@RequestParam String email) {
		authorizeAdminOrManager(email);
		List<ReportDeliveryPerformanceDto> performanceLogs = reportService.getDeliveryPerformance();
		return ResponseEntity.ok(performanceLogs);
	}
}
