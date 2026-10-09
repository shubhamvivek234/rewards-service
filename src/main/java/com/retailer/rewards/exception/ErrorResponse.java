package com.retailer.rewards.exception;

import java.time.Instant;
import java.util.List;

/**
 * Standard structured error response payload returned by API exception handlers.
 */
public record ErrorResponse(
        /**
         * HTTP status code number.
         */
        int status,

        /**
         * HTTP error reason phrase.
         */
        String error,

        /**
         * Human-readable error summary.
         */
        String message,

        /**
         * List of detailed validation errors or diagnostic hints.
         */
        List<String> details,

        /**
         * URI path where the error occurred.
         */
        String path,

        /**
         * Timestamp when the error was captured.
         */
        Instant timestamp
) {
    /**
     * Creates an error response without details.
     *
     * @param status HTTP status code
     * @param error error name
     * @param message error message
     * @param path request path
     * @return constructed {@link ErrorResponse}
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, List.of(), path, Instant.now());
    }

    /**
     * Creates an error response with list of detailed reasons.
     *
     * @param status HTTP status code
     * @param error error name
     * @param message error message
     * @param details list of specific error details
     * @param path request path
     * @return constructed {@link ErrorResponse}
     */
    public static ErrorResponse of(int status, String error, String message, List<String> details, String path) {
        return new ErrorResponse(status, error, message, details, path, Instant.now());
    }
}
