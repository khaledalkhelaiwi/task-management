package com.task_management_mini_system.task_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.task_management_mini_system.task_management.model.Task;
import com.task_management_mini_system.task_management.model.User;
import com.task_management_mini_system.task_management.repository.TaskRepository;
import com.task_management_mini_system.task_management.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import com.task_management_mini_system.task_management.model.enums.Role;

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

    public Task createTask(Task task) {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ((UserDetails) auth.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found: " + currentUsername));

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;

        Long targetUserId;
        if (isAdmin && task.getUser() != null && task.getUser().getId() != null) {
            targetUserId = task.getUser().getId();
        } else {
            targetUserId = currentUser.getId();
        }

        User owner = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id = " + targetUserId));

        task.setUser(owner);
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(LocalDate.now());
        }

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
