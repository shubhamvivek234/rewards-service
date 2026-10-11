package com.retailer.rewards.service;

import com.retailer.rewards.entity.Customer;

import java.util.List;

/**
 * Service interface for customer profile operations.
 */
public interface CustomerService {

    /**
     * Retrieves all registered customers.
     *
     * @return list of customers
     */
    List<Customer> getAllCustomers();

    /**
     * Retrieves a single customer by unique ID.
     *
     * @param id customer identifier
     * @return customer entity
     */
    Customer getCustomerById(Long id);

    /**
     * Creates and saves a new customer.
     *
     * @param customer customer entity to create
     * @return persisted customer entity
     */
    Customer createCustomer(Customer customer);
}
