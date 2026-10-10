package com.retailer.rewards.controller;

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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCustomerRewards() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].customerId", equalTo(1)))
                .andExpect(jsonPath("$.data[0].customerName", equalTo("Alice Johnson")))
                .andExpect(jsonPath("$.data[0].totalRewardPoints", equalTo(415)))
                .andExpect(jsonPath("$.data[0].monthlyRewards", hasSize(3)))
                .andExpect(jsonPath("$.data[1].customerId", equalTo(2)))
                .andExpect(jsonPath("$.data[1].customerName", equalTo("Bob Smith")))
                .andExpect(jsonPath("$.data[1].totalRewardPoints", equalTo(600)))
                .andExpect(jsonPath("$.data[2].customerId", equalTo(3)))
                .andExpect(jsonPath("$.data[2].customerName", equalTo("Carol Davis")))
                .andExpect(jsonPath("$.data[2].totalRewardPoints", equalTo(520)))
                .andExpect(jsonPath("$.data[3].customerId", equalTo(4)))
                .andExpect(jsonPath("$.data[3].customerName", equalTo("David Wilson")))
                .andExpect(jsonPath("$.data[3].totalRewardPoints", equalTo(0)));
    }

    @Test
    void testGetAliceRewardsBreakdown() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.customerId", equalTo(1)))
                .andExpect(jsonPath("$.data.customerName", equalTo("Alice Johnson")))
                .andExpect(jsonPath("$.data.totalRewardPoints", equalTo(415)))
                .andExpect(jsonPath("$.data.totalTransactions", equalTo(6)))
                .andExpect(jsonPath("$.data.monthlyRewards[0].yearMonth", equalTo("2026-07")))
                .andExpect(jsonPath("$.data.monthlyRewards[0].monthName", equalTo("JULY")))
                .andExpect(jsonPath("$.data.monthlyRewards[0].rewardPoints", equalTo(90)))
                .andExpect(jsonPath("$.data.monthlyRewards[0].transactionCount", equalTo(2)))
                .andExpect(jsonPath("$.data.monthlyRewards[1].yearMonth", equalTo("2026-08")))
                .andExpect(jsonPath("$.data.monthlyRewards[1].monthName", equalTo("AUGUST")))
                .andExpect(jsonPath("$.data.monthlyRewards[1].rewardPoints", equalTo(275)))
                .andExpect(jsonPath("$.data.monthlyRewards[1].transactionCount", equalTo(2)))
                .andExpect(jsonPath("$.data.monthlyRewards[2].yearMonth", equalTo("2026-09")))
                .andExpect(jsonPath("$.data.monthlyRewards[2].monthName", equalTo("SEPTEMBER")))
                .andExpect(jsonPath("$.data.monthlyRewards[2].rewardPoints", equalTo(50)))
                .andExpect(jsonPath("$.data.monthlyRewards[2].transactionCount", equalTo(2)));
    }

    @Test
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

    @Test
    void testCustomerNotFoundNegativeScenario() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customers/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Customer not found with ID: 999")));
    }

    @Test
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
