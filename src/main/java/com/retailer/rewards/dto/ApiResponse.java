package com.retailer.rewards.dto;

import java.time.Instant;

/**
 * Standard generic wrapper for REST API responses.
 *
 * @param <T> payload type
 */
public record ApiResponse<T>(
        /**
         * Indicates if the operation was successful.
         */
        boolean success,

        /**
         * Human-readable message describing the outcome.
         */
        String message,

        /**
         * Response payload data.
         */
        T data,

        /**
         * Timestamp of response generation.
         */
        Instant timestamp
) {
    /**
     * Creates a successful API response with data and a custom message.
     *
     * @param message description message
     * @param data response payload
     * @param <T> type of payload
     * @return populated {@link ApiResponse}
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    /**
     * Creates a successful API response with default message.
     *
     * @param data response payload
     * @param <T> type of payload
     * @return populated {@link ApiResponse}
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Operation completed successfully", data, Instant.now());
    }

    /**
     * Creates a successful 201 Created response.
     *
     * @param message description message
     * @param data created payload
     * @param <T> type of payload
     * @return populated {@link ApiResponse}
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }
}
