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
 * Implementation of {@link CustomerService} backed by {@link CustomerRepository}.
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    /**
     * Constructs the service with required repository dependency.
     *
     * @param customerRepository customer repository
     */
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Retrieves all registered customers.
     *
     * @return list of customers
     */
    @Override
    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll();
    }

    /**
     * Retrieves a single customer by ID.
     *
     * @param id customer identifier
     * @return found customer
     */
    @Override
    public Customer getCustomerById(Long id) {
        log.debug("Fetching customer with id={}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> CustomerNotFoundException.forId(id));
    }

    /**
     * Creates a new customer.
     *
     * @param customer entity to persist
     * @return persisted customer
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
