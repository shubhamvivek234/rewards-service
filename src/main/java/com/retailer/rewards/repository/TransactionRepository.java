package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Transaction} entities.
 *
 * <p>Provides methods to query transactions by customer and dynamic date ranges.</p>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions associated with a given customer ID, ordered by transaction date ascending.
     *
     * @param customerId unique identifier of the customer
     * @return list of transactions for the customer
     */
    List<Transaction> findByCustomerIdOrderByTransactionDateAsc(Long customerId);

    /**
     * Finds all transactions for a given customer within an inclusive date range.
     *
     * @param customerId unique identifier of the customer
     * @param startDate start date of the range (inclusive)
     * @param endDate end date of the range (inclusive)
     * @return list of matching transactions ordered by transaction date ascending
     */
    List<Transaction> findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
            Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * Finds all transactions within an inclusive date range across all customers.
     *
     * @param startDate start date of the range (inclusive)
     * @param endDate end date of the range (inclusive)
     * @return list of matching transactions ordered by transaction date ascending
     */
    List<Transaction> findByTransactionDateBetweenOrderByTransactionDateAsc(
            LocalDate startDate, LocalDate endDate);

    /**
     * Fetches all transactions with customer eagerly joined to prevent N+1 query overhead.
     *
     * @return list of all transactions with customer details
     */
    @Query("SELECT t FROM Transaction t JOIN FETCH t.customer ORDER BY t.transactionDate ASC")
    List<Transaction> findAllWithCustomer();

    /**
     * Fetches transactions for a specific customer with customer eagerly joined.
     *
     * @param customerId unique identifier of the customer
     * @return list of transactions with customer details
     */
    @Query("SELECT t FROM Transaction t JOIN FETCH t.customer WHERE t.customer.id = :customerId ORDER BY t.transactionDate ASC")
    List<Transaction> findByCustomerIdWithCustomer(@Param("customerId") Long customerId);
}
