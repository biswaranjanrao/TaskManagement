package com.example.TaskManagement.serviceimplTests;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import com.example.TaskManagement.domain.Task;
import com.example.TaskManagement.domain.TaskStatus;
import com.example.TaskManagement.exception.TaskNotFoundHttpStatusException;
import com.example.TaskManagement.repository.TaskRepository;
import com.example.TaskManagement.service.impl.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

	@Mock
	private TaskRepository taskRepository;
	
	@InjectMocks
	private TaskServiceImpl taskServiceImpl;
	
	private Task mockTask1;
	private Task mockTask2;
	
	@BeforeEach
	void setUp() {
		mockTask1 = new Task("1","Title-1","Description-1",TaskStatus.IN_PROGRESS,LocalDate.of(2025, 11, 23));		
		
		mockTask2 = new Task("2","Title-2","Description-2",TaskStatus.DONE,LocalDate.of(2025, 11, 21));		
	}
	
	@Test
	void getAllTasksSortedByDueDateFromTaskManagement() {
		//Test Case to get all task IDS
		List<Task> unsortedTasks = Arrays.asList(mockTask1,mockTask2);
		when(taskRepository.findAll()).thenReturn(unsortedTasks);
		
		List<Task> sortedTasks = taskServiceImpl.getAllTasks();
		
		verify(taskRepository,times(1)).findAll();
		assertEquals(2,sortedTasks.size());    
	}
	
	@Test
	void getTaskBasedOnTaskID () {
		//Test Case when the task is present and found
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));
		
		Task fetchedTask = taskServiceImpl.getTask("1");
		
		assertNotNull(fetchedTask);
		assertEquals("1",fetchedTask.getId());
		verify(taskRepository,times(1)).findById("1");	
	}
	
	@Test
	void getTaskBasedOnTaskID_ExceptionWhenTaskIdNotPresent() {
		//Test Case when the task is not present
		when (taskRepository.findById("99")).thenReturn(Optional.empty());		
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.getTask("99"); });
		verify(taskRepository,times(1)).findById("99");
	}
	
	@Test
	void createNewTask() {
		when(taskRepository.save(any(Task.class))).thenReturn(mockTask1);
		
		Task createdTask = taskServiceImpl.createTask(mockTask1);
		
		assertNotNull(createdTask);
		verify(taskRepository, times(1)).save(mockTask1);
	}
	
	@Test
	void createNewTask_DueDateNotinFuture() {
		//Testing if Due date is in the future
		Task pastData = new Task("10","Title","Testing future date",TaskStatus.DONE,LocalDate.of(2025, 10, 1));
		assertThrows(IllegalArgumentException.class,()->{taskServiceImpl.createTask(pastData);});
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	@Test
	void createNewTask_TitleMandatoryFieldsCheck() {
		//Testing Mandatory fields exception - Title is null
		Task mandateTitleFieldsTest = new Task("10",null,"Description",TaskStatus.PENDING,LocalDate.of(2025, 12, 1));
		assertThrows(IllegalArgumentException.class,()-> {taskServiceImpl.createTask(mandateTitleFieldsTest);});		
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	@Test
	void createNewTask_DueDateMandatoryFieldCheck() {
		//Testing Mandatory fields exception - DueDate is null
		Task mandateDueDateFieldsTest = new Task("10","Title","Description",TaskStatus.PENDING,null);
		assertThrows(IllegalArgumentException.class,()-> {taskServiceImpl.createTask(mandateDueDateFieldsTest);});
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	@Test
	void updateExisitngTask() {
		//Test Case when task ID is present
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask1);

		Task updateTask = new Task();
		updateTask.setTitle("Test Update Title");
		
		Task update = taskServiceImpl.updateTask("1", updateTask);
		
		assertEquals("Test Update Title",update.getTitle());
		assertEquals("Description-1",update.getDescription());
		assertEquals(TaskStatus.IN_PROGRESS,update.getStatus());
		
		verify(taskRepository, times(1)).save(mockTask1);		
	}
	
	@Test
	void updateExisitngTask_TaskIDNotPresent() {
		//Test Case when task ID is not present
		when(taskRepository.findById("99")).thenReturn(Optional.empty());
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.updateTask("99",new Task()); });
		verify(taskRepository,times(1)).findById("99");
	}
	
	@Test
	void deleteExistingTask() {
		//Test Case when Task ID is present
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));		
		taskServiceImpl.deleteTask("1");		
		verify(taskRepository, times(1)).delete(mockTask1);			
	}
	
	@Test
	void deleteExistingTask_TaskIDNotPresent() {
		//Test Case when Task ID is not present
		when(taskRepository.findById("99")).thenReturn(Optional.empty());
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.deleteTask("99"); });
		verify(taskRepository,times(1)).findById("99");
	}
	
}
