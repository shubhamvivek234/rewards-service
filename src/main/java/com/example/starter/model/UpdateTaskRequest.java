package com.example.starter.model;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters if provided")
    String title,

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description,

    TaskStatus status,

    Priority priority
) {}
