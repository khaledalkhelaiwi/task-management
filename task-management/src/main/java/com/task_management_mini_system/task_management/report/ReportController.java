package com.task_management_mini_system.task_management.report;

import com.task_management_mini_system.task_management.config.JwtUtil;
import com.task_management_mini_system.task_management.model.Task;
import com.task_management_mini_system.task_management.report.dto.TaskReportRow;
import com.task_management_mini_system.task_management.service.TasksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    private final ReportService reportService;
    private final TasksService tasksService;
    private final JwtUtil jwtUtil;

    public ReportController(ReportService reportService, TasksService tasksService, JwtUtil jwtUtil) {
        this.reportService = reportService;
        this.tasksService = tasksService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/api/reports/tasks/my")
    public ResponseEntity<byte[]> myTasksPdf(HttpServletRequest request) {

        String token = jwtUtil.extractTokenFromRequest(request);
        String username = jwtUtil.extractUsername(token);

        List<Task> tasks = tasksService.getMyTasks(username);

        List<TaskReportRow> rows = tasks.stream()
                .map(t -> new TaskReportRow(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getStatus() == null ? "" : t.getStatus().name()
                ))
                .toList(); 

        byte[] pdf = reportService.generateTasksPdf(rows, "My Tasks Report", username);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=my-tasks-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/api/reports/tasks/all")
    public ResponseEntity<byte[]> allTasksPdf() {

        List<Task> tasks = tasksService.getAllTasks();
        
        List<TaskReportRow> rows = tasks.stream()
                .map(t -> new TaskReportRow(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getStatus() == null ? "" : t.getStatus().name()
                ))
                .toList();

        byte[] pdf = reportService.generateTasksPdf(rows, "All Tasks Report", "ADMIN");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-tasks-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
