package com.supplytracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.Shipment;
import com.supplytracker.entity.ShipmentStatus;
import com.supplytracker.entity.User;
import com.supplytracker.exception.ResourceNotFoundException;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.repository.UserRepositry;

@Service
public class ShipmentServiceImpl implements ShipmentService{
	
	@Autowired
	private ShipmentRepository shiprepo;
	
	@Autowired
	private ItemRepository itemrepo;
	
	@Autowired
	private UserRepositry userrepo;
	
	public ShipmentDto addShipment(ShipmentDto dto) {
		Item item = itemrepo.findById(dto.getItemId()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", dto.getItemId()));
		User Transporter = itemrepo.findByName(dto.getAssignedTransporter()).orElseThrow(() -> new ResourceNotFoundException("Transporter", "name", dto.getAssignedTransporter()));
		
		Shipment shipment = new Shipment();
		shipment.setItem(item);
		shipment.setFromLocation(dto.getFromLocation());
		shipment.setToLocation(dto.getToLocation());
		shipment.setExpectedDelivery(dto.getExpectedDelivery());
		shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus().toUpperCase()));
		shipment.setAssignedTransporter(Transporter);
		
		Shipment saved = shiprepo.save(shipment);
		dto.setId(saved.getId());
		return dto;
	}
	@Override
	public ShipmentDto getShipmentById(Long id) {
		Shipment shipment = shiprepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", id));
		return mapToDto(shipment);
	}
	
	@Override
	public List<ShipmentDto> getAllShipments(){
		return shiprepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}
	
	@Override
	public ShipmentDto updateShipment(Long id, ShipmentDto dto) {
		Shipment shipment = shiprepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", id));
		Item item = itemrepo.findById(dto.getItemId()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", dto.getItemId()));
		User Transporter = userrepo.findByEmail(dto.getAssignedTransporter()).orElseThrow(() -> new ResourceNotFoundException("Transporter", "name", dto.getAssignedTransporter()));
		
		shipment.setItem(item);
		shipment.setFromLocation(dto.getFromLocation());
		shipment.setToLocation(dto.getToLocation());
		shipment.setExpectedDelivery(dto.getExpectedDelivery());
		shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus().toUpperCase()));
		shipment.setAssignedTransporter(Transporter);
		shiprepo.save(shipment);
		return mapToDto(shipment);
	}
	
	@Override
	public void deleteShipment(Long id) {
		Shipment shipment = shiprepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", id));
		shiprepo.delete(shipment);
	}
	
	private ShipmentDto mapToDto(Shipment shipment) {
		return new ShipmentDto(
			shipment.getId(),
			shipment.getItem().getId(),
			shipment.getFromLocation(),
			shipment.getToLocation(),
			shipment.getExpectedDelivery(),
			shipment.getCurrentStatus().name(),
			shipment.getAssignedTransporter().getName()
		);
	}
}
