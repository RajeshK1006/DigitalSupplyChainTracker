package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.supplytracker.entity.Role;
import com.supplytracker.entity.User;
import com.supplytracker.exception.ItemNotFoundException;
import com.supplytracker.exception.ShipmentNotFoundException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.service.interfaces.ItemServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ShipmentDto;
import com.supplytracker.entity.Shipment;
import com.supplytracker.entity.ShipmentStatus;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.ShipmentRepository;
import com.supplytracker.repository.UserRepository;

@Service
public class ShippmentService implements ItemServiceInterface.ShipmentService {

    @Autowired
    private ShipmentRepository shiprepo;

    @Autowired
    private ItemRepository itemrepo;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ModelMapper mapper;

    public ShipmentDto addShipment(ShipmentDto dto) {
        Shipment shipment = mapper.map(dto, Shipment.class);

        shipment.setItem(itemrepo.findById(dto.getItemId()).orElseThrow(()-> new ItemNotFoundException("The Item with this id is not Found!!!")));
        User transporter = userrepo.findById(dto.getAssignedTransporterId()).orElseThrow(() -> new UserNotFoundException("User if this id is not Found"));

        if(transporter.getRole()!= Role.TRANSPORTER){
            throw new IllegalArgumentException("Assigned user is not a Transporter");
        }

        shipment.setAssignedTransporter(transporter);

        if (dto.getCurrentStatus() !=null){
            shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus().toUpperCase()));
        }else{
            throw new IllegalArgumentException("Current Status cannot be Null");
    }
        Shipment saved = shiprepo.save(shipment);
        return mapper.map(saved, ShipmentDto.class);
    }


    @Override
    public ShipmentDto getShipmentById(Long id) {
        Shipment shipment = shiprepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("The shipment with the given id is not Found"));
        return mapper.map(shipment, ShipmentDto.class);
    }

    @Override
    public List<ShipmentDto> getAllShipments(){
        List<Shipment> ships  = shiprepo.findAll();
        List<ShipmentDto> res = new ArrayList<>();
        for(Shipment st: ships){
            mapper.map(st, ShipmentDto.class);
        }

        return res;
    }

    @Override
    public ShipmentDto updateShipment(Long id, ShipmentDto dto) {
            Shipment existing_shipment = shiprepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("new Shipment with this id is not found"));

            existing_shipment.setToLocation(dto.getToLocation());
            existing_shipment.setFromLocation(dto.getFromLocation());
            existing_shipment.setExpectedDelivery(dto.getExpectedDelivery());
            existing_shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus()));

            if(dto.getAssignedTransporterId() != null){
                User transporter = userrepo.findById(dto.getAssignedTransporterId()).orElseThrow(()-> new UserNotFoundException("User if not found with this id"));
            }
            shiprepo.save(existing_shipment);
            return mapper.map(existing_shipment, ShipmentDto.class);
    }

    @Override
    public void deleteShipment(Long id) {
        try{
            shiprepo.deleteById(id);
        }
        catch(RuntimeException e){
            throw new ShipmentNotFoundException("The shippment with the give id s Not found");
        }
    }

}