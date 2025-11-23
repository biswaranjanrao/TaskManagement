package com.example.TaskManagement.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TaskManagement.domain.Task;
import com.example.TaskManagement.exception.TaskNotFoundHttpStatusException;
import com.example.TaskManagement.repository.TaskRepository;
import com.example.TaskManagement.service.TaskService;
//This class is responsible for handing business logic operations

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	//This method gives the business logic for retrieving all the tasks present and sorts it based on Due_date
	@Override
	public List<Task> getAllTasks() {
		return taskRepository.findAll().stream()
				.sorted(Comparator.comparing(Task::getDue_date))				
				.collect(Collectors.toList());
	}
	
	/*This method gives the business logic for fetching tasks based on task id provided in the Path Variable.
	 * If the task is not found, it throws exception (404 Not Found) which is handled by Global Exception Handler class
	 */
	@Override
	public Task getTask(String id) {
		Task task = taskRepository.findById(id)
					.orElseThrow(()-> new TaskNotFoundHttpStatusException(id));					
		return task;				
	}
	
	//This method is responsible to create new tasks
	@Override
	public Task createTask(Task task) {
		//Date validation
		if(task.getDue_date() != null && task.getDue_date().isBefore(LocalDate.now())){
			throw new java.lang.IllegalArgumentException("Due date should be greater than current Date");
		}
		//Mandatory fields validation
		if(task.getTitle()==null || task.getDue_date()==null) {
			throw new java.lang.IllegalArgumentException("Mandatory fields (Title,Due Date) are missing entries");
		}
		Task addTask = taskRepository.save(task);
		return addTask;
	}
	
	//This method gives the business logic in updating the existing tasks
	@Override
	public Task updateTask(String id, Task task) {
		Task existingTask = taskRepository.findById(id)
							.orElseThrow(()-> new TaskNotFoundHttpStatusException(id));
		if(task.getTitle()!= null)
		{
			existingTask.setTitle(task.getTitle());
		}
		if(task.getDescription()!= null)
		{
			existingTask.setDescription(task.getDescription());
		}
		if(task.getStatus()!= null)
		{
			existingTask.setStatus(task.getStatus());
		}
		if(task.getDue_date()!= null)
		{
			existingTask.setDue_date(task.getDue_date());
		}
		return taskRepository.save(existingTask);
	}
	
	//This method gives the business logic for deleting tasks based on task ID
	@Override
	public void deleteTask(String id) {
		Task exisitingTask = taskRepository.findById(id)
							 .orElseThrow(()-> new TaskNotFoundHttpStatusException(id));
		taskRepository.delete(exisitingTask);	
	}	

}
