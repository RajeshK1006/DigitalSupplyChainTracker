package com.supplytracker.controller;

import com.supplytracker.dto.ReportDelayedSupplyDto;
import com.supplytracker.dto.ReportDeliveryPerformanceDto;
import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserRepository userRepository;

	// Check if the user is ADMIN or MANAGER before accessing reports
	private void authorizeAdminOrManager(String email) {
		logger.info("Authorizing user role for report access");

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("User not found for report access");
					return new UserNotFoundException("User with this email is not found");
				});

		String role = String.valueOf(user.getRole()).toUpperCase();
		if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
			logger.warn("Unauthorized access attempt to reports");
			throw new InvalidRoleException("Only ADMINs and MANAGERs are authorized to access this report");
		}

		logger.info("User authorized for report access");
	}

	// Endpoint to get list of delayed shipments (no role check required)
	@GetMapping("/delayed-shipments")
	public ResponseEntity<List<ReportDelayedSupplyDto>> getDelayedShipments() {
		logger.info("Fetching delayed shipments report");
		List<ReportDelayedSupplyDto> delayedShipments = reportService.getDelayedShipments();
		return ResponseEntity.ok(delayedShipments);
	}

	// Endpoint to get delivery performance report, only accessible by ADMIN or MANAGER
	@GetMapping("/delivery-performance")
	public ResponseEntity<List<ReportDeliveryPerformanceDto>> getDeliveryPerformance(@RequestParam String email) {
		logger.info("Request received for delivery performance report");
		authorizeAdminOrManager(email);
		List<ReportDeliveryPerformanceDto> performanceLogs = reportService.getDeliveryPerformance();
		return ResponseEntity.ok(performanceLogs);
	}
}
