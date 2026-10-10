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

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        log.debug("Fetching customer with id={}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> CustomerNotFoundException.forId(id));
    }

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
