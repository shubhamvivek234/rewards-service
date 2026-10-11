package com.retailer.rewards.exception;

/**
 * Exception thrown when a customer cannot be found by identifier.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Constructs exception with error message.
     *
     * @param message error description
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }

    /**
     * Factory method creating exception for a specific customer ID.
     *
     * @param id customer identifier
     * @return customer not found exception instance
     */
    public static CustomerNotFoundException forId(Long id) {
        return new CustomerNotFoundException("Customer not found with ID: " + id);
    }
}
