package com.example.TaskManagement.exception;

//This class is responsible to handle and send an appropriate error message if task id is not available
public class TaskNotFoundHttpStatusException extends RuntimeException{
	
	public TaskNotFoundHttpStatusException (String id) {
		super ("Task ID Not found");
	}

}
