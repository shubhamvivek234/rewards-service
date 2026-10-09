package com.example.starter.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    int status,
    String error,
    String message,
    List<String> details,
    String path,
    Instant timestamp
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, List.of(), path, Instant.now());
    }

    public static ErrorResponse of(int status, String error, String message, List<String> details, String path) {
        return new ErrorResponse(status, error, message, details, path, Instant.now());
    }
}
