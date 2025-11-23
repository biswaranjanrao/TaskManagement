package com.example.TaskManagement.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.TaskManagement.controller.TaskController;
import com.example.TaskManagement.domain.Task;
import com.example.TaskManagement.domain.TaskStatus;
import com.example.TaskManagement.service.TaskService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
	
	@Autowired
	private MockMvc mockMvc; // Mocks HTTPS Requests
	
	@MockitoBean
    private TaskService taskService; // Mocks Service Layer

    @Autowired
    private ObjectMapper objectMapper; // 
    
    private Task mockTask; //Mock Test Data
    
    //Initializing mock data to mockTask variable
    @BeforeEach
    void setUp() {
    	mockTask = new Task("1","Title-1","Description - 1",TaskStatus.IN_PROGRESS,LocalDate.of(2025, 11, 23));    	 	
    }
    
    //JUnit test case for creating a task
    @Test
    void createTask () throws Exception {
    	when(taskService.createTask(any(Task.class))).thenReturn(mockTask);
    	
    	mockMvc.perform(post("/tasks")
    		   .contentType(MediaType.APPLICATION_JSON)
    		   .content(objectMapper.writeValueAsString(mockTask)))
    		   .andExpect(status().isCreated())
    		   .andExpect(jsonPath("$.title").value("Title-1"))
               .andExpect(jsonPath("$.id").value("1"));
    		   
    	verify(taskService, times(1)).createTask(any(Task.class));
    }
    
  //JUnit test case for fetching a task based on Task ID
    @Test
    void getTaskonID () throws Exception {
    	when(taskService.getTask("1")).thenReturn(mockTask);
    	
    	mockMvc.perform(get("/tasks/{id}","1")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(mockTask)))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.title").value("Title-1"))
    			.andExpect(jsonPath("$.id").value("1"));
    	
    	verify(taskService, times(1)).getTask("1");    	
    }
    
    //JUnit test case for updating a task
    @Test
    void updateTaskonID() throws Exception {
    	Task updateTask = new Task();
    	updateTask.setDescription("Test Description");
    	updateTask.setTitle("Test Title");
    	when(taskService.updateTask(eq("1"), any(Task.class))).thenReturn(updateTask);
    	
    	mockMvc.perform(put("/tasks/{id}","1")
    		   .contentType(MediaType.APPLICATION_JSON)
    		   .content(objectMapper.writeValueAsString(updateTask)))
    		   .andExpect(status().isOk())
    		   .andExpect(jsonPath("$.description").value("Test Description"))
    		   .andExpect(jsonPath("$.title").value("Test Title"));
    	
    	verify(taskService, times(1)).updateTask(eq("1"), any(Task.class));    	
    }
    
  //JUnit test case for deleting a task
    @Test
    void deleteTaskById () throws Exception {
    	doNothing().when(taskService).deleteTask("1");
    	mockMvc.perform(delete("/tasks/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask("1");
    }
    
    //JUnit test case for fetching all the tasks
    @Test
    void getAllTasks () throws Exception {
    	Task mockTask2 = new Task();
    	mockTask2.setId("2");
    	mockTask2.setTitle("Title-2");    	
    	List<Task> allTasks = Arrays.asList(mockTask,mockTask2);
    	
    	when(taskService.getAllTasks()).thenReturn(allTasks);
    	
    	mockMvc.perform(get("/tasks")
    		   .contentType(MediaType.APPLICATION_JSON))
    		   .andExpect(status().isOk())
    		   .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].id").value("1"))
               .andExpect(jsonPath("$[1].id").value("2"));
    	
    	verify(taskService,times(1)).getAllTasks();
    		   
    }

}
