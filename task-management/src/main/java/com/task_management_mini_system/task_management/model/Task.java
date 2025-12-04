package com.task_management_mini_system.task_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task_management_mini_system.task_management.model.enums.TaskStatus;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @Column(nullable = false) 
    private  String title;
   
    

    private String description;
    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();
    

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

   public Task(){}

    public Task( String title, String description, TaskStatus status, LocalDate createdAt, User user) {
		super();

		this.title = title;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.user = user;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return  user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
	@Override
	public String toString() {
		return "tasks [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", createdAt=" + createdAt + ", user=" + user + "]";
	}
	
}
