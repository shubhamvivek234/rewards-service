package com.example.starter.service;

import com.example.starter.exception.ResourceNotFoundException;
import com.example.starter.model.CreateTaskRequest;
import com.example.starter.model.Priority;
import com.example.starter.model.TaskItem;
import com.example.starter.model.TaskStatus;
import com.example.starter.model.UpdateTaskRequest;
import com.example.starter.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskItem> getTasks(TaskStatus status, Priority priority, String search) {
        log.debug("Fetching tasks with status={}, priority={}, search={}", status, priority, search);
        return repository.findByFilters(status, priority, search);
    }

    @Override
    public TaskItem getTaskById(String id) {
        log.debug("Fetching task by id={}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
    }

    @Override
    public TaskItem createTask(CreateTaskRequest request) {
        String generatedId = "task-" + UUID.randomUUID().toString().substring(0, 8);
        Instant now = Instant.now();

        TaskItem newTask = new TaskItem(
                generatedId,
                request.title().trim(),
                request.description() != null ? request.description().trim() : "",
                TaskStatus.PENDING,
                request.priority(),
                now,
                now
        );

        log.info("Created new task with id={}", generatedId);
        return repository.save(newTask);
    }

    @Override
    public TaskItem updateTask(String id, UpdateTaskRequest request) {
        TaskItem existing = getTaskById(id);

        TaskItem updated = existing.withUpdates(
                request.title() != null ? request.title().trim() : null,
                request.description() != null ? request.description().trim() : null,
                request.status(),
                request.priority()
        );

        log.info("Updated task with id={}", id);
        return repository.save(updated);
    }

    @Override
    public TaskItem updateTaskStatus(String id, TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        TaskItem existing = getTaskById(id);
        TaskItem updated = existing.withStatus(status);

        log.info("Updated status of task id={} to {}", id, status);
        return repository.save(updated);
    }

    @Override
    public void deleteTask(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with ID: " + id);
        }
        repository.deleteById(id);
        log.info("Deleted task with id={}", id);
    }
}
