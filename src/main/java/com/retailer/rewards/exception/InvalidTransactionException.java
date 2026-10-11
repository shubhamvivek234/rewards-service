package com.retailer.rewards.exception;

/**
 * Exception thrown when a transaction payload or date range fails business validation.
 */
public class InvalidTransactionException extends RuntimeException {

    /**
     * Constructs exception with validation error message.
     *
     * @param message validation error description
     */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
