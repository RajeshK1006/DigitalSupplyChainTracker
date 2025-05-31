package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.List;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.entity.Shipment;
import com.supplytracker.entity.ShipmentStatus;
import com.supplytracker.exception.ItemNotFoundException;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.interfaces.ItemServiceInterface;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShippmentService implements ItemServiceInterface.ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShippmentService.class);

    @Autowired
    private ShipmentRepository shiprepo;

    @Autowired
    private ItemRepository itemrepo;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ModelMapper mapper;

    // Add a new shipment
    public ShipmentDto addShipment(ShipmentDto dto) {
        logger.info("Adding shipment for item ID: {}", dto.getItemId());

        Shipment shipment = mapper.map(dto, Shipment.class);

        // Find item or throw if not found
        shipment.setItem(itemrepo.findById(dto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("The Item with this id is not Found!!!")));

        // Find assigned transporter or throw if not found
        User transporter = userrepo.findById(dto.getAssignedTransporterId())
                .orElseThrow(() -> new UserNotFoundException("User if this id is not Found"));

        // Verify assigned user is a transporter
        if (transporter.getRole() != Role.TRANSPORTER) {
            logger.error("User with ID {} is not a Transporter", dto.getAssignedTransporterId());
            throw new IllegalArgumentException("Assigned user is not a Transporter");
        }

        shipment.setAssignedTransporter(transporter);

        // Validate current status is provided
        if (dto.getCurrentStatus() != null) {
            shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus().toUpperCase()));
        } else {
            logger.error("Current Status cannot be null");
            throw new IllegalArgumentException("Current Status cannot be Null");
        }

        Shipment saved = shiprepo.save(shipment);
        logger.info("Shipment added with ID: {}", saved.getId());
        return mapper.map(saved, ShipmentDto.class);
    }

    // Get shipment by id
    @Override
    public ShipmentDto getShipmentById(Long id) {
        logger.info("Fetching shipment by ID: {}", id);
        Shipment shipment = shiprepo.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("The shipment with the given id is not Found"));
        return mapper.map(shipment, ShipmentDto.class);
    }

    // Get all shipments
    @Override
    public List<ShipmentDto> getAllShipments() {
        logger.info("Fetching all shipments");
        List<Shipment> ships = shiprepo.findAll();
        List<ShipmentDto> res = new ArrayList<>();
        for (Shipment st : ships) {
            res.add(mapper.map(st, ShipmentDto.class));
        }
        return res;
    }

    // Update shipment details
    @Override
    public ShipmentDto updateShipment(Long id, ShipmentDto dto) {
        logger.info("Updating shipment with ID: {}", id);
        Shipment existing_shipment = shiprepo.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with this id is not found"));

        if (dto.getToLocation() != null) {
            existing_shipment.setToLocation(dto.getToLocation());
        }

        if (dto.getFromLocation() != null) {
            existing_shipment.setFromLocation(dto.getFromLocation());
        }

        if (dto.getExpectedDelivery() != null) {
            existing_shipment.setExpectedDelivery(dto.getExpectedDelivery());
        }

        if (dto.getCurrentStatus() != null) {
            existing_shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus()));
        }

        if (dto.getAssignedTransporterId() != null) {
            User transporter = userrepo.findById(dto.getAssignedTransporterId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with this id"));
            existing_shipment.setAssignedTransporter(transporter);
        }

        shiprepo.save(existing_shipment);
        logger.info("Shipment updated with ID: {}", id);
        return mapper.map(existing_shipment, ShipmentDto.class);
    }

    // Delete shipment by id
    @Override
    public void deleteShipment(Long id) {
        logger.info("Deleting shipment with ID: {}", id);
        try {
            shiprepo.deleteById(id);
            logger.info("Shipment deleted with ID: {}", id);
        } catch (RuntimeException e) {
            logger.error("Failed to delete shipment with ID: {}", id, e);
            throw new ShipmentNotFoundException("The shipment with the given id is not found");
        }
    }

}
