package com.example.TaskManagement.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Entity class to define Task Parameters
 * Using Lombok library to reduce boilerplate of code
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "TaskId", nullable = false)
	private String id; 
	
	@Column(name = "TaskTitle", nullable = false) //Ensures title is mandatorily filled when adding a new task
	private String title;
	
	@Column(name = "TaskDescription")
	private String description;
	
	@Column(name = "TaskStatus")
	private TaskStatus status;
	
	@Column(name = "DueDate", nullable = false)//Ensures due date is mandatorily filled when adding a new task
	private LocalDate due_date;

}
