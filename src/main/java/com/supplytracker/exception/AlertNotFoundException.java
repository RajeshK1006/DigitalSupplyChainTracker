package com.supplytracker.exception;

// Creating Custom exception to handle cases where an alert is not found in the database
public class AlertNotFoundException extends RuntimeException{
    public AlertNotFoundException(String err){
        super(err);
    }
}
