package com.supplytracker.repository;

import com.supplytracker.entity.CheckpointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface CheckpointLogRepository extends JpaRepository<CheckpointLog, Long> {
	List<CheckpointLog> findByShipmentIdOrderByTimestampAsc(Long shipmentId);
	
}


