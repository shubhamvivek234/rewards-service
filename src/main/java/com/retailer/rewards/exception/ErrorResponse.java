package com.retailer.rewards.exception;

import java.time.Instant;
import java.util.List;

/**
 * Standard error response envelope returned by global exception handling.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        List<String> details,
        String path,
        Instant timestamp
) {
    /**
     * Creates an error response without supplementary details.
     *
     * @param status  HTTP status code
     * @param error   HTTP status reason phrase
     * @param message error summary message
     * @param path    request URI
     * @return error response envelope
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, List.of(), path, Instant.now());
    }

    /**
     * Creates an error response including detailed validation messages.
     *
     * @param status  HTTP status code
     * @param error   HTTP status reason phrase
     * @param message error summary message
     * @param details list of field-level validation errors
     * @param path    request URI
     * @return error response envelope
     */
    public static ErrorResponse of(int status, String error, String message, List<String> details, String path) {
        return new ErrorResponse(status, error, message, details, path, Instant.now());
    }
}
