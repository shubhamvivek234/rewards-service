package com.retailer.rewards.exception;

/**
 * Exception thrown when a transaction payload contains invalid or logically inconsistent data.
 */
public class InvalidTransactionException extends RuntimeException {

    /**
     * Constructs a new InvalidTransactionException with detail message.
     *
     * @param message description of the validation failure
     */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
