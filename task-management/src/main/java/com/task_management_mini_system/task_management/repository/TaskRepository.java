package com.task_management_mini_system.task_management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.task_management_mini_system.task_management.model.Task;
import com.task_management_mini_system.task_management.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);   
}