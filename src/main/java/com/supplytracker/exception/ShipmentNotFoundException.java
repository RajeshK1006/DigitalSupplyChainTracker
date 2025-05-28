package com.supplytracker.exception;

public class ShipmentNotFoundException extends RuntimeException{
    public ShipmentNotFoundException(String err){
        super(err);
    }
}
