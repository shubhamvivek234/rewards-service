package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO response containing aggregated customer reward points and monthly breakdown.
 */
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
