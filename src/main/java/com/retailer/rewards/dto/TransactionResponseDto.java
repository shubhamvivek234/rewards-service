package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        Long id,
        Long customerId,
        String customerName,
        BigDecimal amount,
        LocalDate transactionDate,
        String description,
        Long rewardPoints,
        LocalDateTime createdAt
) {
}
