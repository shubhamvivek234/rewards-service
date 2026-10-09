package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a single recorded transaction and its earned points.
 */
public record TransactionResponseDto(
        /**
         * Unique transaction identifier.
         */
        Long id,

        /**
         * Associated customer identifier.
         */
        Long customerId,

        /**
         * Full name of the customer.
         */
        String customerName,

        /**
         * Purchase amount in USD.
         */
        BigDecimal amount,

        /**
         * Date of the transaction.
         */
        LocalDate transactionDate,

        /**
         * Brief purchase description.
         */
        String description,

        /**
         * Reward points awarded for this transaction.
         */
        Long rewardPoints,

        /**
         * Record creation timestamp.
         */
        LocalDateTime createdAt
) {
}
