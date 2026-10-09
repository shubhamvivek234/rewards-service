package com.retailer.rewards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for recording a new customer purchase transaction.
 */
public record CreateTransactionRequest(
        /**
         * Unique ID of the customer making the purchase.
         */
        @NotNull(message = "Customer ID is required")
        Long customerId,

        /**
         * Purchase amount in USD. Must be strictly positive.
         */
        @NotNull(message = "Purchase amount is required")
        @DecimalMin(value = "0.01", message = "Purchase amount must be greater than zero")
        BigDecimal amount,

        /**
         * Date of the purchase. Cannot be in the future.
         */
        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        LocalDate transactionDate,

        /**
         * Optional description of the purchase items.
         */
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description
) {
}
