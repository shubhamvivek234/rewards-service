package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.util.List;

public record CustomerRewardsResponse(
        Long customerId,
        String customerName,
        String customerEmail,
        List<MonthlyRewardDto> monthlyRewards,
        long totalRewardPoints,
        BigDecimal totalAmountSpent,
        long totalTransactions
) {
}
