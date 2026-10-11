package com.retailer.rewards.dto;

import java.math.BigDecimal;

/**
 * DTO representing monthly reward metrics for a customer.
 */
public record MonthlyRewardDto(
        String yearMonth,
        String monthName,
        int year,
        long transactionCount,
        BigDecimal totalSpent,
        long rewardPoints
) {
}
