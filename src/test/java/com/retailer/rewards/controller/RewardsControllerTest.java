package com.retailer.rewards.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link RewardsController} verifying endpoints against
 * the SQL test dataset pre-loaded in the H2 database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies retrieving rewards for all customers in the seed dataset.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/rewards/customers should return summaries for all 4 seed customers")
    void testGetAllCustomerRewards() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data", hasSize(4)))
                // Verify Alice (ID=1): total 415 points across 3 months
                .andExpect(jsonPath("$.data[0].customerId", equalTo(1)))
                .andExpect(jsonPath("$.data[0].customerName", equalTo("Alice Johnson")))
                .andExpect(jsonPath("$.data[0].totalRewardPoints", equalTo(415)))
                .andExpect(jsonPath("$.data[0].monthlyRewards", hasSize(3)))
                // Verify Bob (ID=2): total 600 points across 3 months
                .andExpect(jsonPath("$.data[1].customerId", equalTo(2)))
                .andExpect(jsonPath("$.data[1].customerName", equalTo("Bob Smith")))
                .andExpect(jsonPath("$.data[1].totalRewardPoints", equalTo(600)))
                // Verify Carol (ID=3): total 520 points across 3 months
                .andExpect(jsonPath("$.data[2].customerId", equalTo(3)))
                .andExpect(jsonPath("$.data[2].customerName", equalTo("Carol Davis")))
                .andExpect(jsonPath("$.data[2].totalRewardPoints", equalTo(520)))
                // Verify David (ID=4): 0 points, 0 transactions
                .andExpect(jsonPath("$.data[3].customerId", equalTo(4)))
                .andExpect(jsonPath("$.data[3].customerName", equalTo("David Wilson")))
                .andExpect(jsonPath("$.data[3].totalRewardPoints", equalTo(0)));
    }

    /**
     * Verifies retrieving reward details for Customer 1 (Alice Johnson)
     * including exact dynamic monthly breakdown for July, August, and September 2026.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/rewards/customers/1 should return exact 3-month breakdown and 415 total points")
    void testGetAliceRewardsBreakdown() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.customerId", equalTo(1)))
                .andExpect(jsonPath("$.data.customerName", equalTo("Alice Johnson")))
                .andExpect(jsonPath("$.data.totalRewardPoints", equalTo(415)))
                .andExpect(jsonPath("$.data.totalTransactions", equalTo(6)))
                // Month 1: July 2026 -> 90 points ($120 @ 90pts + $45 @ 0pts)
                .andExpect(jsonPath("$.data.monthlyRewards[0].yearMonth", equalTo("2026-07")))
                .andExpect(jsonPath("$.data.monthlyRewards[0].monthName", equalTo("JULY")))
                .andExpect(jsonPath("$.data.monthlyRewards[0].rewardPoints", equalTo(90)))
                .andExpect(jsonPath("$.data.monthlyRewards[0].transactionCount", equalTo(2)))
                // Month 2: August 2026 -> 275 points ($200 @ 250pts + $75 @ 25pts)
                .andExpect(jsonPath("$.data.monthlyRewards[1].yearMonth", equalTo("2026-08")))
                .andExpect(jsonPath("$.data.monthlyRewards[1].monthName", equalTo("AUGUST")))
                .andExpect(jsonPath("$.data.monthlyRewards[1].rewardPoints", equalTo(275)))
                .andExpect(jsonPath("$.data.monthlyRewards[1].transactionCount", equalTo(2)))
                // Month 3: September 2026 -> 50 points ($100 @ 50pts + $50 @ 0pts)
                .andExpect(jsonPath("$.data.monthlyRewards[2].yearMonth", equalTo("2026-09")))
                .andExpect(jsonPath("$.data.monthlyRewards[2].monthName", equalTo("SEPTEMBER")))
                .andExpect(jsonPath("$.data.monthlyRewards[2].rewardPoints", equalTo(50)))
                .andExpect(jsonPath("$.data.monthlyRewards[2].transactionCount", equalTo(2)));
    }

    /**
     * Verifies retrieving reward details for Customer 4 (David Wilson) who has zero transactions.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/rewards/customers/4 should return 0 points and empty monthly list")
    void testGetCustomerWithNoTransactions() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.customerId", equalTo(4)))
                .andExpect(jsonPath("$.data.totalRewardPoints", equalTo(0)))
                .andExpect(jsonPath("$.data.totalTransactions", equalTo(0)))
                .andExpect(jsonPath("$.data.monthlyRewards", hasSize(0)));
    }

    /**
     * Negative scenario: verifying 404 response when querying a non-existent customer.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/rewards/customers/999 should return 404 Not Found")
    void testCustomerNotFoundNegativeScenario() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Customer not found with ID: 999")));
    }

    /**
     * Negative scenario: verifying 400 Bad Request when startDate is after endDate.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/rewards/customers/1 with invalid date range should return 400 Bad Request")
    void testInvalidDateRangeNegativeScenario() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/1")
                        .param("startDate", "2026-09-30")
                        .param("endDate", "2026-07-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")));
    }
}
