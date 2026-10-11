package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Transaction} entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for a given customer ordered chronologically ascending.
     *
     * @param customerId customer identifier
     * @return list of matching transactions
     */
    List<Transaction> findByCustomerIdOrderByTransactionDateAsc(Long customerId);

    /**
     * Finds transactions for a customer within a date range ordered chronologically ascending.
     *
     * @param customerId customer identifier
     * @param startDate  range start date (inclusive)
     * @param endDate    range end date (inclusive)
     * @return list of matching transactions
     */
    List<Transaction> findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
            Long customerId, LocalDate startDate, LocalDate endDate);
}
