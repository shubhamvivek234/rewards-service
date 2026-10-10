package com.retailer.rewards.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public static CustomerNotFoundException forId(Long id) {
        return new CustomerNotFoundException("Customer not found with ID: " + id);
    }
}
