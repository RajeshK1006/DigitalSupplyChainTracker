package com.supplytracker.exception;

public class ItemNotFoundException extends RuntimeException {
	
	public ItemNotFoundException(String error) {
		super(error);
	}

}
