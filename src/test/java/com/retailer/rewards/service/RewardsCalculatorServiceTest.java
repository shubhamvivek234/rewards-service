package com.retailer.rewards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RewardsCalculatorServiceTest {

    private RewardsCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new RewardsCalculatorServiceImpl();
    }

    @Test
    void testAssignmentExampleOneHundredTwentyDollars() {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(120.00));
        assertThat(points).isEqualTo(90L);
    }

    @ParameterizedTest
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
    void testTieredCalculations(double amount, long expectedPoints) {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(amount));
        assertThat(points).isEqualTo(expectedPoints);
    }

    @Test
    void testNullAmountReturnsZero() {
        long points = calculatorService.calculatePoints((BigDecimal) null);
        assertThat(points).isEqualTo(0L);
    }

    @Test
    void testNegativeAmountReturnsZero() {
        long points = calculatorService.calculatePoints(BigDecimal.valueOf(-50.00));
        assertThat(points).isEqualTo(0L);
    }

    @Test
    void testFractionalCentsFlooring() {
        long points1 = calculatorService.calculatePoints(BigDecimal.valueOf(120.99));
        assertThat(points1).isEqualTo(90L);

        long points2 = calculatorService.calculatePoints(BigDecimal.valueOf(50.99));
        assertThat(points2).isEqualTo(0L);

        long points3 = calculatorService.calculatePoints(BigDecimal.valueOf(100.99));
        assertThat(points3).isEqualTo(50L);
    }

    @Test
    void testDoubleOverload() {
        long points = calculatorService.calculatePoints(120.0);
        assertThat(points).isEqualTo(90L);
    }
}
