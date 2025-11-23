package com.example.TaskManagement.domainTest;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.TaskManagement.domain.Task;
import com.example.TaskManagement.domain.TaskStatus;

public class TaskTest {
	
	//Declaration and initialization of constrains
	private final LocalDate TODAY = LocalDate.now();
    private final UUID TEST_UUID = UUID.randomUUID();
    private final String TEST_ID = TEST_UUID.toString(); 

    private Task createTestTask() {
    	Task task = new Task();
    	task.setId(TEST_ID);
    	task.setTitle("Test title");
    	task.setDescription("Test description");
    	task.setStatus(TaskStatus.PENDING);
    	task.setDue_date(TODAY);
    	return task;
        
    }
    
    //JUnit test case for NoArgsConstructor
    @Test
    void testNoArgsConstructorAndSetters() {
      
        Task task = new Task();
        String newTitle = "New Title";
        LocalDate tomorrow = TODAY.plusDays(1);
        
        task.setId(TEST_ID);
        task.setTitle(newTitle);
        task.setDescription("New description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDue_date(tomorrow);

        assertEquals(TEST_ID, task.getId());
        assertEquals(newTitle, task.getTitle());
        assertEquals("New description", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(tomorrow, task.getDue_date());
    }    
    
    //JUnit test case of equals and hashcode for compliance of entity with JPA
    @Test
    void testEqualsAndHashCode() {
    	Task task1 = createTestTask();        
        Task task2 = createTestTask();        
        Task task3 = new Task();
        
        task3.setId(UUID.randomUUID().toString());
        task3.setTitle("Test title -2");
        task3.setDescription("Test description -2");
        task3.setStatus(TaskStatus.DONE);
        task3.setDue_date(TODAY.plusDays(5));
        
        assertEquals(task1, task1);       
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1, task3);
        assertNotEquals(task1.hashCode(), task3.hashCode());        
        assertNotEquals(task1, null);
    }
}


