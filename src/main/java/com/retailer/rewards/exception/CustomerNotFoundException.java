package com.retailer.rewards.exception;

/**
 * Exception thrown when a requested customer is not found in the system.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Constructs a new CustomerNotFoundException with detail message.
     *
     * @param message description of the error
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }

    /**
     * Convenience factory for customer ID lookup failure.
     *
     * @param id missing customer identifier
     * @return constructed exception instance
     */
    public static CustomerNotFoundException forId(Long id) {
        return new CustomerNotFoundException("Customer not found with ID: " + id);
    }
}
