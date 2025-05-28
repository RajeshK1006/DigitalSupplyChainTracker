package com.supplytracker.service;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.supplytracker.exception.ShipmentNotFoundException;
import org.modelmapper.ModelMapper;
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
import com.supplytracker.repository.UserRepository;

@Service
public class ShippmentServiceImplementation implements ShipmentService{

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

        if (dto.getCurrentStatus() !=null){
            shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus().toUpperCase()));
        }else{
            throw new IllegalArgumentException("Current Status cannot be Null");
    }
        shiprepo.save(shipment);
        return dto;
    }


    @Override
    public ShipmentDto getShipmentById(Long id) {
        Shipment shipment = shiprepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("The shippment with the given id is not Found"));
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
            Shipment existing_shipment = shiprepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("new Shippment with this id is not found"));
            existing_shipment.setToLocation(dto.getToLocation());
            existing_shipment.setFromLocation(dto.getFromLocation());
            existing_shipment.setExpectedDelivery(dto.getExpectedDelivery());
            existing_shipment.setCurrentStatus(ShipmentStatus.valueOf(dto.getCurrentStatus()));
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