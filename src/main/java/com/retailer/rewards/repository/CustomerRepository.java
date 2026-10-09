package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Customer} entities.
 *
 * <p>Provides standard CRUD operations and custom query methods for customer records.</p>
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a customer by their unique email address.
     *
     * @param email customer email address
     * @return an {@link Optional} containing the found customer, or empty if not found
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Checks if a customer exists with the given email address.
     *
     * @param email customer email address
     * @return true if customer exists, false otherwise
     */
    boolean existsByEmail(String email);
}
