package com.example.starter.model;

import java.time.Instant;

public record TaskItem(
    String id,
    String title,
    String description,
    TaskStatus status,
    Priority priority,
    Instant createdAt,
    Instant updatedAt
) {
    public TaskItem withUpdates(String title, String description, TaskStatus status, Priority priority) {
        return new TaskItem(
            this.id(),
            title != null ? title : this.title(),
            description != null ? description : this.description(),
            status != null ? status : this.status(),
            priority != null ? priority : this.priority(),
            this.createdAt(),
            Instant.now()
        );
    }

    public TaskItem withStatus(TaskStatus newStatus) {
        return new TaskItem(
            this.id(),
            this.title(),
            this.description(),
            newStatus != null ? newStatus : this.status(),
            this.priority(),
            this.createdAt(),
            Instant.now()
        );
    }
}
