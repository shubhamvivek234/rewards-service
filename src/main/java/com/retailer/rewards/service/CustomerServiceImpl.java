package com.retailer.rewards.service;

import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link CustomerService} providing customer management operations.
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    /**
     * Constructs the customer service with repository dependency.
     *
     * @param customerRepository customer repository
     */
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Returns all customers.
     *
     * @return list of customer entities
     */
    @Override
    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll();
    }

    /**
     * Finds a customer by ID or throws {@link CustomerNotFoundException}.
     *
     * @param id customer identifier
     * @return customer entity
     */
    @Override
    public Customer getCustomerById(Long id) {
        log.debug("Fetching customer with id={}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> CustomerNotFoundException.forId(id));
    }

    /**
     * Persists a new customer record with timestamp initialization.
     *
     * @param customer customer entity to save
     * @return saved customer entity
     */
    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(LocalDateTime.now());
        }
        log.info("Creating new customer with email={}", customer.getEmail());
        return customerRepository.save(customer);
    }
}
