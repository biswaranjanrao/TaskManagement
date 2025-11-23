package com.example.TaskManagement.service;

import java.util.List;

import com.example.TaskManagement.domain.Task;

//This interface is responsible for providing Service Layer for the Controller to interact and process Buisness Logic

public interface TaskService {
	
	List<Task> getAllTasks(); // Method to retireve all tasks
	
	Task getTask(String id); // Method to retrieve task based on task ID

	Task createTask(Task task); //Method to create a new task

	Task updateTask(String id, Task task); //Method to update an existing task

	void deleteTask(String id); //Method to delete an existing task
	
	
	

}
