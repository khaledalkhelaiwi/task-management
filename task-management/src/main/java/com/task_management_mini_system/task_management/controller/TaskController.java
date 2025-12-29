package com.task_management_mini_system.task_management.controller;

import com.task_management_mini_system.task_management.model.Task;
import com.task_management_mini_system.task_management.service.TasksService;
import com.task_management_mini_system.task_management.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TasksService taskService;
    private final JwtUtil jwtUtil;

    public TaskController(TasksService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task,HttpServletRequest request) {


    	String token = jwtUtil.extractTokenFromRequest(request);


        String username = jwtUtil.extractUsername(token);


        Task saved = taskService.createTask(task);

        return ResponseEntity.ok(saved);
    }
    

    @GetMapping("/api/tasks/my")
    public ResponseEntity<?> getMyTasks(HttpServletRequest request) {

        String token = jwtUtil.extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(token);

        return ResponseEntity.ok(taskService.getMyTasks(username));
    }

    
    @GetMapping("/api/tasks/all")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    

    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask));
    }
    

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
}
