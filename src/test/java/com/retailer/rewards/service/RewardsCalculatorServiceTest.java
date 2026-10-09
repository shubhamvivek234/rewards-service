package com.retailer.rewards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive unit tests for {@link RewardsCalculatorServiceImpl}.
 *
 * <p>Validates point calculation rules across standard amounts, tier thresholds,
 * fractional cent boundaries, and edge cases.</p>
 */
class RewardsCalculatorServiceTest {

    private RewardsCalculatorService calculatorService;

    /**
     * Initializes the service under test before each test run.
     */
    @BeforeEach
    void setUp() {
        calculatorService = new RewardsCalculatorServiceImpl();
    }

    /**
     * Verifies the exact example specified in the assignment:
     * $120 purchase = 2 x $20 + 1 x $50 = 90 points.
     */
    @Test
    @DisplayName("Should award 90 points for $120 purchase as specified in assignment problem statement")
    void testAssignmentExampleOneHundredTwentyDollars() {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(120.00));
        assertThat(points).isEqualTo(90L);
    }

    /**
     * Parameterized test verifying core tier amounts and thresholds.
     *
     * @param amount purchase dollar amount
     * @param expectedPoints expected points awarded
     */
    @ParameterizedTest(name = "Purchase of ${0} should award {1} points")
    @CsvSource({
            "0.00, 0",
            "25.00, 0",
            "49.99, 0",
            "50.00, 0",
            "51.00, 1",
            "75.00, 25",
            "90.00, 40",
            "100.00, 50",
            "101.00, 52",
            "120.00, 90",
            "150.00, 150",
            "200.00, 250",
            "250.00, 350",
            "300.00, 450"
    })
    @DisplayName("Should award exact points matching tiered calculation formula")
    void testTieredCalculations(double amount, long expectedPoints) {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(amount));
        assertThat(points).isEqualTo(expectedPoints);
    }

    /**
     * Verifies handling of null amount inputs.
     */
    @Test
    @DisplayName("Should return 0 points when amount is null")
    void testNullAmountReturnsZero() {
        long points = calculatorService.calculatePoints((BigDecimal) null);
        assertThat(points).isEqualTo(0L);
    }

    /**
     * Verifies negative monetary purchase amounts return 0 points.
     */
    @Test
    @DisplayName("Should return 0 points when amount is negative")
    void testNegativeAmountReturnsZero() {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(-50.00));
        assertThat(points).isEqualTo(0L);
    }

    /**
     * Verifies fractional cents flooring behavior.
     */
    @Test
    @DisplayName("Should floor fractional cents according to whole dollar policy")
    void testFractionalCentsFlooring() {
        // $120.99 should floor to $120 -> 90 points
        long points1 = calculatorService.calculatePoints(BigDecimal.valueOf(120.99));
        assertThat(points1).isEqualTo(90L);

        // $50.99 should floor to $50 -> 0 points
        long points2 = calculatorService.calculatePoints(BigDecimal.valueOf(50.99));
        assertThat(points2).isEqualTo(0L);

        // $100.99 should floor to $100 -> 50 points
        long points3 = calculatorService.calculatePoints(BigDecimal.valueOf(100.99));
        assertThat(points3).isEqualTo(50L);
    }

    /**
     * Verifies overloaded primitive double method yields identical result.
     */
    @Test
    @DisplayName("Double overload should match BigDecimal calculation")
    void testDoubleOverload() {
        long points = calculatorService.calculatePoints(120.0);
        assertThat(points).isEqualTo(90L);
    }
}
