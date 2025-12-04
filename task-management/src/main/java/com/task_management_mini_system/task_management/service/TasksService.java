package com.task_management_mini_system.task_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.task_management_mini_system.task_management.model.Task;
import com.task_management_mini_system.task_management.model.User;
import com.task_management_mini_system.task_management.repository.TaskRepository;
import com.task_management_mini_system.task_management.repository.UserRepository;

@Service
public class TasksService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TasksService(TaskRepository taskRepository,
                        UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getMyTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return taskRepository.findByUser(user);
    }

    public Task createTask(Task task, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        task.setUser(user);      
        return taskRepository.save(task);
    }
    
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {

        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id = " + id));

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());
        existing.setCreatedAt(updatedTask.getCreatedAt());

        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id = " + id);
        }
        taskRepository.deleteById(id);
    }
    
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id = " + id));
    }
}
