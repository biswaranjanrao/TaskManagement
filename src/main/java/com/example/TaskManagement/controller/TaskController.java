package com.example.TaskManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TaskManagement.domain.Task;
import com.example.TaskManagement.service.TaskService;

import jakarta.validation.Valid;

//Controller Class to manage CRUD operations for Task Management

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	/*
	 * CREATE TASK - This method is responsible to create new Tasks
	 * Input Parameters  - Title,Description,Status,DueDate (Title and Due Date are mandatory)
	 * Parameters - ID,Title,Description,Status,DueDate
	 * Validation - On Success - 201 Created, On failure - 400 Bad Request
	 */
	@PostMapping
	public ResponseEntity<Task> createTask(@Valid @RequestBody Task task){
		Task createdTask = taskService.createTask(task);
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}
	
	/*
	 * GET TASK - This method is responsible for fetching tasks based on task ID
	 * Input Parameters - ID (as Path Variable)
	 * Response - ID,Title,Description,Status,DueDate
	 * Validation - On Success - 200 OK, On failure - 404 Not Found
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTask(@PathVariable String id){
		Task getTask = taskService.getTask(id);
		return new ResponseEntity<>(getTask,HttpStatus.OK);
	}
	
	/*
	 * UPDATE TASK - This method is responsible for updating tasks based on the task ID
	 * Input Parameters - ID (As path variable),Title,Description,Status,DueDate
	 * Response - ID,Title,Description,Status,DueDate
	 * Validation - On Success - 200 OK, On Failure - 404 Not Found
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask (@PathVariable String id, @RequestBody Task task){
		Task updatedTask = taskService.updateTask(id, task);
		return new ResponseEntity<>(updatedTask,HttpStatus.OK);
	}
	
	/*
	 * DELETE TASK - This method is responsible for deleting tasks based on the Task ID
	 * Input Parameters - ID (As path variable)
	 * Response - null
	 * Validation - On success 204 No Content, On failure - 404 Not found
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask (@PathVariable String id){
		taskService.deleteTask(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*
	 * GET ALL TASKS - This method is responsible for fetching all the tasks
	 * Input Parameters - none
	 * Response - All the tasks with their respective ID,Title,Description,Status,DueDate
	 * Validation - On Success - 200 OK, On failure - 404 Not Found
	 */
	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks() {
		List<Task> getAllTasks = taskService.getAllTasks();
		return new ResponseEntity<>(getAllTasks,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
}
