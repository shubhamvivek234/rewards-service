package com.retailer.rewards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of {@link RewardsCalculatorService} implementing the retailer rewards algorithm.
 *
 * <p>Points Calculation Logic:</p>
 * <pre>
 *   If amount &lt;= 50:
 *       Points = 0
 *   If 50 &lt; amount &lt;= 100:
 *       Points = (amount - 50) * 1
 *   If amount &gt; 100:
 *       Points = (amount - 100) * 2 + 50 * 1
 * </pre>
 */
@Service
public class RewardsCalculatorServiceImpl implements RewardsCalculatorService {

    private static final Logger log = LoggerFactory.getLogger(RewardsCalculatorServiceImpl.class);

    private static final BigDecimal FIRST_THRESHOLD = BigDecimal.valueOf(50);
    private static final BigDecimal SECOND_THRESHOLD = BigDecimal.valueOf(100);
    private static final long POINTS_FOR_FIRST_TIER = 50L; // 1 point for every dollar between 50 and 100

    /**
     * Default constructor for RewardsCalculatorServiceImpl.
     */
    public RewardsCalculatorServiceImpl() {
    }

    /**
     * Calculates reward points for a purchase amount.
     *
     * <p>The dollar amount is floored to the nearest whole integer dollar value
     * to reward each full dollar spent according to retailer policy.</p>
     *
     * @param amount monetary transaction amount in USD
     * @return reward points earned (non-negative)
     */
    @Override
    public long calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(FIRST_THRESHOLD) <= 0) {
            return 0L;
        }

        // Floor the decimal to consider whole dollar amounts
        long totalDollars = amount.setScale(0, RoundingMode.FLOOR).longValue();
        long points = 0L;

        if (totalDollars > SECOND_THRESHOLD.longValue()) {
            // 2 points for every dollar spent over $100
            long overHundred = totalDollars - SECOND_THRESHOLD.longValue();
            points += overHundred * 2;

            // Plus 1 point for every dollar spent between $50 and $100 (which is 50 points)
            points += POINTS_FOR_FIRST_TIER;
        } else if (totalDollars > FIRST_THRESHOLD.longValue()) {
            // 1 point for every dollar spent between $50 and $100
            points += (totalDollars - FIRST_THRESHOLD.longValue());
        }

        log.debug("Calculated {} reward points for purchase amount of ${}", points, amount);
        return points;
    }

    /**
     * Overloaded method accepting a primitive double amount.
     *
     * @param amount monetary transaction amount in USD
     * @return reward points earned
     */
    @Override
    public long calculatePoints(double amount) {
        return calculatePoints(BigDecimal.valueOf(amount));
    }
}
