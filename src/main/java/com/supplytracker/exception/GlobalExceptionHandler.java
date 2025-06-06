package com.supplytracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
		
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());

		return new  ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidRoleException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRoleExceptions(InvalidRoleException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AlertNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAlertNotFoundExceptions(AlertNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CheckPointNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCheckPointNotFoundExceptions(CheckPointNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ShipmentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleShipmentNotFoundExceptions(ShipmentNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(ResourceNotFoundException e){
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error: "+ e.getMessage(), LocalDateTime.now());

		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
