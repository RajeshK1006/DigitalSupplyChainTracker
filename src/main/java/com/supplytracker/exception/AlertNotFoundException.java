package com.supplytracker.exception;

public class AlertNotFoundException extends RuntimeException{
    public AlertNotFoundException(String err){
        super(err);
    }
}
