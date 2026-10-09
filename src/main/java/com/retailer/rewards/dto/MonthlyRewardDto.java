package com.retailer.rewards.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing the reward points summary for a specific month.
 *
 * <p>All month information is computed dynamically from transaction dates and is
 * never hard-coded.</p>
 */
public record MonthlyRewardDto(
        /**
         * Standard year-month identifier (e.g., "2026-07").
         */
        String yearMonth,

        /**
         * Full human-readable month name (e.g., "JULY").
         */
        String monthName,

        /**
         * Calendar year (e.g., 2026).
         */
        int year,

        /**
         * Number of recorded transactions in this month.
         */
        long transactionCount,

        /**
         * Total monetary amount spent in this month.
         */
        BigDecimal totalSpent,

        /**
         * Total reward points earned in this month.
         */
        long rewardPoints
) {
}
