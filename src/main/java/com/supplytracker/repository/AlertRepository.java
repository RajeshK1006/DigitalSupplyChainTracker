package com.supplytracker.repository;


import com.supplytracker.entity.Alert;
<<<<<<< HEAD
=======
import com.supplytracker.entity.AlertType;
>>>>>>> feature/user-login
import com.supplytracker.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*
 * Checking if the alert already exists or not for that particular Shipment and Type
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
<<<<<<< HEAD
	boolean existsByShipmentAndType(Shipment shipment, String type);
=======
	boolean existsByShipmentAndType(Shipment shipment, AlertType type);
>>>>>>> feature/user-login
}
