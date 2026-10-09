package com.example.starter.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required and cannot be blank")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    String title,

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description,

    @NotNull(message = "Priority is required (LOW, MEDIUM, HIGH, URGENT)")
    Priority priority
) {}
