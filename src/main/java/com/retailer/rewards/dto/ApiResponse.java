package com.retailer.rewards.dto;

import java.time.Instant;

/**
 * Generic standard API response wrapper for consistent REST payloads.
 *
 * @param <T> payload data type
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {
    /**
     * Creates a success response with HTTP 200 semantics.
     *
     * @param message descriptive status message
     * @param data    response body data
     * @param <T>     data type
     * @return standardized API response
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    /**
     * Creates a success response with HTTP 201 semantics.
     *
     * @param message descriptive status message
     * @param data    response body data
     * @param <T>     data type
     * @return standardized API response
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }
}
