package com.supplytracker.exception;

public class InvalidRoleException extends RuntimeException{
    public InvalidRoleException(String err){
        super(err);
    }
}
