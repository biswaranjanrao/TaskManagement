package com.example.TaskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * This class is responsible for handling errors throughout the project.
 * It majorily handles TASK NOT FOUND (Triggered when task id is not available) 
 * and BAD REQUESTS ( Triggered when Mandatory fields are not entered while adding new tasks)
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TaskNotFoundHttpStatusException.class)
	public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundHttpStatusException e) {
		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	}	
	@ExceptionHandler(java.lang.IllegalArgumentException.class)
	public ResponseEntity<String> handleDataValidation (java.lang.IllegalArgumentException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}

}
