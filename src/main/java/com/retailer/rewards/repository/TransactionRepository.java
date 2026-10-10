package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdOrderByTransactionDateAsc(Long customerId);

    List<Transaction> findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
            Long customerId, LocalDate startDate, LocalDate endDate);
}
