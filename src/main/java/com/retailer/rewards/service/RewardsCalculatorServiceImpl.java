package com.retailer.rewards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RewardsCalculatorServiceImpl implements RewardsCalculatorService {

    private static final Logger log = LoggerFactory.getLogger(RewardsCalculatorServiceImpl.class);

    private static final BigDecimal FIRST_THRESHOLD = BigDecimal.valueOf(50);
    private static final BigDecimal SECOND_THRESHOLD = BigDecimal.valueOf(100);
    private static final long POINTS_FOR_FIRST_TIER = 50L;

    @Override
    public long calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(FIRST_THRESHOLD) <= 0) {
            return 0L;
        }

        long totalDollars = amount.setScale(0, RoundingMode.FLOOR).longValue();
        long points = 0L;

        if (totalDollars > SECOND_THRESHOLD.longValue()) {
            long overHundred = totalDollars - SECOND_THRESHOLD.longValue();
            points += overHundred * 2;
            points += POINTS_FOR_FIRST_TIER;
        } else if (totalDollars > FIRST_THRESHOLD.longValue()) {
            points += (totalDollars - FIRST_THRESHOLD.longValue());
        }

        log.debug("Calculated {} reward points for purchase amount of ${}", points, amount);
        return points;
    }

    @Override
    public long calculatePoints(double amount) {
        return calculatePoints(BigDecimal.valueOf(amount));
    }
}
