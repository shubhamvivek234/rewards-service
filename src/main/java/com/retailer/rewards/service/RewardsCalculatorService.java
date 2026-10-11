package com.retailer.rewards.service;

import java.math.BigDecimal;

/**
 * Service interface for calculating customer reward points from purchase amounts.
 */
public interface RewardsCalculatorService {

    /**
     * Calculates reward points based on a purchase amount in BigDecimal.
     *
     * @param amount the purchase transaction amount
     * @return calculated reward points
     */
    long calculatePoints(BigDecimal amount);

    /**
     * Calculates reward points based on a purchase amount in double.
     *
     * @param amount the purchase transaction amount
     * @return calculated reward points
     */
    long calculatePoints(double amount);
}
