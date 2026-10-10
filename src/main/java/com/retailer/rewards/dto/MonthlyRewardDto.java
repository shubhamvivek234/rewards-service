package com.retailer.rewards.dto;

import java.math.BigDecimal;

public record MonthlyRewardDto(
        String yearMonth,
        String monthName,
        int year,
        long transactionCount,
        BigDecimal totalSpent,
        long rewardPoints
) {
}
