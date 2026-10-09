package com.example.starter.controller;

import com.example.starter.model.ApiResponse;
import com.example.starter.model.CreateTaskRequest;
import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;
import com.example.starter.model.UpdateTaskRequest;
import com.example.starter.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskItem>>> listTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String search
    ) {
        List<TaskItem> tasks = taskService.getTasks(status, priority, search);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved " + tasks.size() + " tasks", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskItem>> getTask(@PathVariable String id) {
        TaskItem task = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.ok("Task retrieved", task));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskItem>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskItem created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Task created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskItem>> updateTask(
            @PathVariable String id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        TaskItem updated = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Task updated successfully", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskItem>> updateStatus(
            @PathVariable String id,
            @RequestParam TaskStatus status
    ) {
        TaskItem updated = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Task status updated to " + status, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.ok("Task " + id + " deleted successfully", null));
    }
}
