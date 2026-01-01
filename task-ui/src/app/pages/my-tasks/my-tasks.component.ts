import { Component, OnInit } from '@angular/core';
import { Task, TaskService } from '../../services/task.service';
import { ReportService } from '../../services/report.service';

type TaskStatus = 'NEW' | 'IN_PROGRESS' | 'DONE';

@Component({
  selector: 'app-my-tasks',
  templateUrl: './my-tasks.component.html',
  styleUrls: ['./my-tasks.component.css']
})
export class MyTasksComponent implements OnInit {

  tasks: Task[] = [];
  isLoading = false;
  errorMessage = '';

  isDownloading = false;

  statuses: TaskStatus[] = ['NEW', 'IN_PROGRESS', 'DONE'];

  constructor(
    private taskService: TaskService,
    private reportService: ReportService
  ) {}

  ngOnInit(): void {
    this.loadMyTasks();
  }

  loadMyTasks(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.taskService.getMyTasks().subscribe({
      next: (data) => {
        this.tasks = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load tasks.';
        console.log(err);
      }
    });
  }

  downloadPdf(): void {
    this.isDownloading = true;

    this.reportService.downloadMyTasksPdf().subscribe({
      next: (blob) => {
        const file = new Blob([blob], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'my-tasks-report.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

        window.URL.revokeObjectURL(url);
        this.isDownloading = false;
      },
      error: (err) => {
        this.isDownloading = false;

        if (err?.status === 401) alert('Unauthorized: Please login again.');
        else if (err?.status === 403) alert('Forbidden: No permission.');
        else alert('Failed to download PDF.');

        console.log(err);
      }
    });
  }


changeStatus(task: Task, newStatus: 'NEW' | 'IN_PROGRESS' | 'DONE') {
  const oldStatus = task.status;

  const updatedTask: Task = {
    ...task,
    status: newStatus
  };

  this.taskService.updateTask(task.id!, updatedTask).subscribe({
    next: (saved) => {
      task.status = saved.status; 
    },
    error: (err) => {
      task.status = oldStatus;

      if (err?.status === 401) alert('Session expired, login again.');
      else if (err?.status === 403) alert('Not allowed.');
      else alert('Failed to update status');

      console.log(err);
    }
  });
}
}
