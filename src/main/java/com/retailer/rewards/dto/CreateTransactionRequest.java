package com.retailer.rewards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @NotNull(message = "Customer ID is required")
        Long customerId,

        @NotNull(message = "Purchase amount is required")
        @DecimalMin(value = "0.01", message = "Purchase amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        LocalDate transactionDate,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description
) {
}
