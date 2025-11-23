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
        return new Task(
                TEST_ID,
                "Test title",
                "Test description",
                TaskStatus.PENDING,
                TODAY
        );
    }
    
    //JUnit test case for AllArgsConstructor by creating and verifying all fields of the task
    @Test
    void testAllArgsConstructorAndGetters() {
        Task task = createTestTask();
        
        assertEquals(TEST_ID, task.getId());
        assertEquals("Test title", task.getTitle());
        assertEquals("Test description", task.getDescription());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals(TODAY, task.getDue_date());
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
        Task task2 = new Task(
                TEST_ID,
                "Test title",
                "Test description",
                TaskStatus.PENDING,
                TODAY
        );
        Task task3 = new Task(
                UUID.randomUUID().toString(),
                "Test title -2",
                "Test description -2",
                TaskStatus.DONE,
                TODAY.plusDays(5)
        );
        
        assertEquals(task1, task1);       
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1, task3);
        assertNotEquals(task1.hashCode(), task3.hashCode());        
        assertNotEquals(task1, null);
    }
}


