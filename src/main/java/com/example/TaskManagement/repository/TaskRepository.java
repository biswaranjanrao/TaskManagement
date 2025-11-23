package com.example.TaskManagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TaskManagement.domain.Task;

//This interface is responsible for handling DB operations - SELECT,UPDATE,INSERT,DELETE operations

@Repository
public interface TaskRepository extends JpaRepository <Task,String>{
	
	
}
