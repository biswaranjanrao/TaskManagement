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
	
	//Initialization of mock data before start of each Test methods
	@BeforeEach
	void setUp() {
		mockTask1 = new Task();
		mockTask1.setId("1");
		mockTask1.setTitle("Title-1");
		mockTask1.setDescription("Description-1");
		mockTask1.setStatus(TaskStatus.IN_PROGRESS);
		mockTask1.setDue_date(LocalDate.of(2025, 12, 23));
		
		mockTask2 = new Task();
		mockTask2.setId("2");
		mockTask2.setTitle("Title-2");
		mockTask2.setDescription("Description-2");
		mockTask2.setStatus(TaskStatus.DONE);
		mockTask2.setDue_date(LocalDate.of(2025, 12, 21));
	}
	
	//JUnit Test Case to get all task IDS
	@Test
	void getAllTasksSortedByDueDateFromTaskManagement() {		
		List<Task> unsortedTasks = Arrays.asList(mockTask1,mockTask2);
		when(taskRepository.findAll()).thenReturn(unsortedTasks);
		
		List<Task> sortedTasks = taskServiceImpl.getAllTasks();
		
		verify(taskRepository,times(1)).findAll();
		assertEquals(2,sortedTasks.size());    
	}
	
	//JUnit Test Case when the task is present and found
	@Test
	void getTaskBasedOnTaskID () {
		//Test Case when the task is present and found
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));
		
		Task fetchedTask = taskServiceImpl.getTask("1");
		
		assertNotNull(fetchedTask);
		assertEquals("1",fetchedTask.getId());
		verify(taskRepository,times(1)).findById("1");	
	}
	
	//JUnit Test Case when the task is not present
	@Test
	void getTaskBasedOnTaskID_ExceptionWhenTaskIdNotPresent() {		
		when (taskRepository.findById("99")).thenReturn(Optional.empty());
		
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.getTask("99"); });
		
		verify(taskRepository,times(1)).findById("99");
	}
	
	//JUnit Test case to create a new task
	@Test
	void createNewTask() {
		when(taskRepository.save(any(Task.class))).thenReturn(mockTask1);
		
		Task createdTask = taskServiceImpl.createTask(mockTask1);
		
		assertNotNull(createdTask);
		verify(taskRepository, times(1)).save(mockTask1);
	}
	
	//JUnit test case for Due Date not in future
	@Test
	void createNewTask_DueDateNotinFuture() {		
		Task pastData = new Task();
		
		pastData.setId("10");
		pastData.setTitle("Title");
		pastData.setDescription("Testing future date");
		pastData.setStatus(TaskStatus.DONE);
		pastData.setDue_date(LocalDate.of(2025, 10, 1));
		
		assertThrows(IllegalArgumentException.class,()->{taskServiceImpl.createTask(pastData);});
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	//JUnit test case for Mandatory fields exception - Title is null
	@Test
	void createNewTask_TitleMandatoryFieldsCheck() {		
		Task mandateTitleFieldsTest = new Task();
		
		mandateTitleFieldsTest.setId("10");
		mandateTitleFieldsTest.setTitle(null); 
		mandateTitleFieldsTest.setDescription("Description");
		mandateTitleFieldsTest.setStatus(TaskStatus.PENDING);
		mandateTitleFieldsTest.setDue_date(LocalDate.of(2025, 12, 1));
		
		assertThrows(IllegalArgumentException.class,()-> {taskServiceImpl.createTask(mandateTitleFieldsTest);});		
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	//JUnit test case for Mandatory fields exception -  DueDate is null
	@Test
	void createNewTask_DueDateMandatoryFieldCheck() {
		Task mandateDueDateFieldsTest = new Task();
		
		mandateDueDateFieldsTest.setId("10");
		mandateDueDateFieldsTest.setTitle("Title"); 
		mandateDueDateFieldsTest.setDescription("Description");
		mandateDueDateFieldsTest.setStatus(TaskStatus.PENDING);
		mandateDueDateFieldsTest.setDue_date(null);
		
		assertThrows(IllegalArgumentException.class,()-> {taskServiceImpl.createTask(mandateDueDateFieldsTest);});
		verify(taskRepository, never()).save(any(Task.class));
	}
	
	//JUnit test case for setting status to default value if null
	void createNewTask_DefaultStatusCheck() {
		Task defaultStatus = new Task();
		
		assertEquals(TaskStatus.PENDING,defaultStatus.getStatus());
	}
	
	//JUnit Test Case when task ID is present
	@Test
	void updateExisitngTask() {
		
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
	
	//JUnit Test Case when task ID is not present
	@Test
	void updateExisitngTask_TaskIDNotPresent() {		
		when(taskRepository.findById("99")).thenReturn(Optional.empty());
		
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.updateTask("99",new Task()); });
		
		verify(taskRepository,times(1)).findById("99");
	}
	
	//JUnit test case when due date is updated less than current date
	void updateDueDateFutureableCheck() {
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));
		
		Task updateDueDate = new Task();
		updateDueDate.setDue_date(LocalDate.of(2024, 11, 23));
		
		assertThrows(IllegalArgumentException.class,() -> {taskServiceImpl.updateTask("1",updateDueDate);});
		
		verify(taskRepository,times(1)).findById("1");
		verify(taskRepository,never()).save(any(Task.class));
	}
	
	//JUnit Test Case when Task ID is present
	@Test
	void deleteExistingTask() {		
		when(taskRepository.findById("1")).thenReturn(Optional.of(mockTask1));	
		
		taskServiceImpl.deleteTask("1");		
		
		verify(taskRepository, times(1)).delete(mockTask1);			
	}
	
	//JUnit Test Case when Task ID is not present
	@Test
	void deleteExistingTask_TaskIDNotPresent() {
		when(taskRepository.findById("99")).thenReturn(Optional.empty());
		
		assertThrows(TaskNotFoundHttpStatusException.class,() -> { taskServiceImpl.deleteTask("99"); });
		
		verify(taskRepository,times(1)).findById("99");
	}
	
}
