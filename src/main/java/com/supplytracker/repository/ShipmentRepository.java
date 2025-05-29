package com.supplytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supplytracker.entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>{

	Shipment find(Shipment shipment);
	
}