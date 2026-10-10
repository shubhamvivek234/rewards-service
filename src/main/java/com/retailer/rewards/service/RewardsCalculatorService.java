package com.retailer.rewards.service;

import java.math.BigDecimal;

public interface RewardsCalculatorService {

    long calculatePoints(BigDecimal amount);

    long calculatePoints(double amount);
}
