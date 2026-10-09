package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object representing the complete reward points summary for a customer.
 *
 * <p>Contains both the monthly breakdown (dynamically calculated for each month
 * present in the transactions dataset) and the grand total points earned.</p>
 */
public record CustomerRewardsResponse(
        /**
         * Unique identifier of the customer.
         */
        Long customerId,

        /**
         * Full name of the customer.
         */
        String customerName,

        /**
         * Email address of the customer.
         */
        String customerEmail,

        /**
         * Breakdown of reward points earned per month.
         */
        List<MonthlyRewardDto> monthlyRewards,

        /**
         * Total reward points earned across all considered months.
         */
        long totalRewardPoints,

        /**
         * Total monetary amount spent across all considered transactions.
         */
        BigDecimal totalAmountSpent,

        /**
         * Total count of transactions considered.
         */
        long totalTransactions
) {
}
