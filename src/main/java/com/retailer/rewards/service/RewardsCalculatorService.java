package com.retailer.rewards.service;

import java.math.BigDecimal;

/**
 * Service dedicated to calculating customer reward points based on purchase transaction amounts.
 *
 * <p>Rewards Business Rules:</p>
 * <ul>
 *   <li>A customer receives 2 points for every dollar spent over $100 in each transaction.</li>
 *   <li>A customer receives 1 point for every dollar spent between $50 and $100 in each transaction.</li>
 *   <li>Transactions of $50 or less earn 0 reward points.</li>
 *   <li>Example: A $120 purchase = 2 x $20 + 1 x $50 = 90 points.</li>
 * </ul>
 */
public interface RewardsCalculatorService {

    /**
     * Calculates reward points for a purchase amount provided as {@link BigDecimal}.
     *
     * @param amount monetary transaction amount in USD
     * @return calculated reward points
     */
    long calculatePoints(BigDecimal amount);

    /**
     * Convenience method for calculating reward points for a double purchase amount.
     *
     * @param amount monetary transaction amount in USD
     * @return calculated reward points
     */
    long calculatePoints(double amount);
}
