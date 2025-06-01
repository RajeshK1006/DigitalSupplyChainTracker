package com.supplytracker.exception;

public class CheckPointNotFoundException extends RuntimeException{
    public CheckPointNotFoundException(String err){
        super(err);
    }
}
