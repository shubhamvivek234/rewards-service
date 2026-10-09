package com.retailer.rewards.service;

import com.retailer.rewards.entity.Customer;

import java.util.List;

/**
 * Service managing customer profile operations.
 */
public interface CustomerService {

    /**
     * Retrieves all registered customers.
     *
     * @return list of all customers
     */
    List<Customer> getAllCustomers();

    /**
     * Retrieves a single customer by unique ID.
     *
     * @param id customer identifier
     * @return found {@link Customer}
     * @throws com.retailer.rewards.exception.CustomerNotFoundException if no customer exists with given ID
     */
    Customer getCustomerById(Long id);

    /**
     * Creates and persists a new customer.
     *
     * @param customer customer entity to save
     * @return persisted {@link Customer} entity
     */
    Customer createCustomer(Customer customer);
}
