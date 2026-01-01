import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { ReportService } from '../../services/report.service';

type Status = 'NEW' | 'IN_PROGRESS' | 'DONE';

@Component({
  selector: 'app-admin-tasks',
  templateUrl: './admin-tasks.component.html',
  styleUrls: ['./admin-tasks.component.css']
})
export class AdminTasksComponent implements OnInit {

  tasks: any[] = [];
  isLoading = false;
  errorMessage = '';
  isDownloading = false;

  // ✅ خيارات الستيت (بتستخدمها في select)
  statuses: Status[] = ['NEW', 'IN_PROGRESS', 'DONE'];

  // ✅ فورم إنشاء تاسك
  form = {
    title: '',
    description: '',
    status: 'NEW' as Status,
    userId: '' // نخليه string عشان input
  };

  constructor(
    private taskService: TaskService,
    private reportService: ReportService
  ) {}

  ngOnInit(): void {
    this.loadAllTasks();
  }

  // =========================
  // 1) LOAD ALL TASKS
  // =========================
  loadAllTasks(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.taskService.getAllTasks().subscribe({
      next: (data: any) => {
        this.tasks = data || [];
        this.isLoading = false;
      },
      error: (err: any) => {
        this.isLoading = false;
        this.errorMessage =
          err?.status === 403 ? 'Access denied: Admin only.' : 'Failed to load tasks.';
        console.log(err);
      }
    });
  }

  // =========================
  // 2) CREATE TASK
  // =========================
  createTask(): void {
    const userIdNum = Number(this.form.userId);

    if (!this.form.title.trim() || !this.form.description.trim() || !userIdNum) {
      alert('Please fill Title, Description, and User ID');
      return;
    }

    const payload: any = {
      title: this.form.title,
      description: this.form.description,
      status: this.form.status,
      user: { id: userIdNum }  // ✅ one-to-many: task belongs to user
    };

    this.taskService.createTask(payload).subscribe({
      next: (created: any) => {
        // نخلي الجديد فوق
        this.tasks.unshift(created);

        // reset الفورم
        this.form = { title: '', description: '', status: 'NEW', userId: '' };
      },
      error: (err: any) => {
        alert('Failed to create task');
        console.log(err);
      }
    });
  }

  // =========================
  // 3) UPDATE TASK (title/desc/status/user)
  // =========================
  updateTask(task: any): void {
    const payload = { ...task };

    this.taskService.updateTask(task.id, payload).subscribe({
      next: () => {
        alert('Task updated ✅');
      },
      error: (err: any) => {
        alert('Failed to update task');
        console.log(err);
      }
    });
  }

  // =========================
  // 4) CHANGE STATUS (using PUT)
  // =========================
  changeStatus(task: any, newStatus: Status): void {
    const oldStatus = task.status;

    task.status = newStatus;

    const payload = { ...task };

    this.taskService.updateTask(task.id, payload).subscribe({
      next: () => {
        // ok
      },
      error: (err: any) => {
        task.status = oldStatus;
        alert('Failed to update status');
        console.log(err);
      }
    });
  }

  // =========================
  // 5) DELETE TASK
  // =========================
  deleteTask(taskId: number): void {
    if (!confirm('Are you sure you want to delete this task?')) return;

    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(t => t.id !== taskId);
      },
      error: (err: any) => {
        alert('Failed to delete task');
        console.log(err);
      }
    });
  }

  // =========================
  // 6) DOWNLOAD PDF (JASPER)
  // =========================
  downloadPdf(): void {
    this.isDownloading = true;

    this.reportService.downloadAllTasksPdf().subscribe({
      next: (blob: Blob) => {
        const file = new Blob([blob], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'all-tasks-report.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

        window.URL.revokeObjectURL(url);
        this.isDownloading = false;
      },
      error: (err: any) => {
        this.isDownloading = false;
        alert('Failed to download PDF.');
        console.log(err);
      }
    });
  }
}
