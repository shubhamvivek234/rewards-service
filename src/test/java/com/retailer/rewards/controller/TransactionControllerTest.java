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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link TransactionController} verifying transaction retrieval,
 * recording, Bean Validation, and negative error scenarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies retrieving all seed transactions.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/transactions should return all preloaded transactions")
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(10))));
    }

    /**
     * Verifies filtering transactions by customer ID.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("GET /api/v1/transactions?customerId=1 should return Alice's 6 transactions")
    void testGetTransactionsByCustomerId() throws Exception {
        mockMvc.perform(get("/api/v1/transactions")
                        .param("customerId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data", hasSize(6)));
    }

    /**
     * Verifies successfully creating a new transaction and computing its points.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("POST /api/v1/transactions should record transaction and return 201 Created with 90 points")
    void testCreateTransactionSuccess() throws Exception {
        String payload = """
                {
                    "customerId": 1,
                    "amount": 120.00,
                    "transactionDate": "2026-09-25",
                    "description": "Weekend Retail Shopping"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.customerId", equalTo(1)))
                .andExpect(jsonPath("$.data.amount", equalTo(120.00)))
                .andExpect(jsonPath("$.data.rewardPoints", equalTo(90)));
    }

    /**
     * Negative scenario: verifying 400 Bad Request when amount is zero or negative.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("POST /api/v1/transactions with negative amount should return 400 Bad Request")
    void testCreateTransactionNegativeAmount() throws Exception {
        String invalidPayload = """
                {
                    "customerId": 1,
                    "amount": -50.00,
                    "transactionDate": "2026-09-25",
                    "description": "Invalid Refund"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")));
    }

    /**
     * Negative scenario: verifying 400 Bad Request when transaction date is in the future.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("POST /api/v1/transactions with future date should return 400 Bad Request")
    void testCreateTransactionFutureDate() throws Exception {
        String futurePayload = """
                {
                    "customerId": 1,
                    "amount": 100.00,
                    "transactionDate": "2099-01-01",
                    "description": "Future Purchase"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(futurePayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")));
    }

    /**
     * Negative scenario: verifying 404 Not Found when customer does not exist.
     *
     * @throws Exception if request execution fails
     */
    @Test
    @DisplayName("POST /api/v1/transactions for non-existent customer should return 404 Not Found")
    void testCreateTransactionNonExistentCustomer() throws Exception {
        String unknownCustomerPayload = """
                {
                    "customerId": 9999,
                    "amount": 120.00,
                    "transactionDate": "2026-09-25",
                    "description": "Order for unknown customer"
                }
                """;

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unknownCustomerPayload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")));
    }
}
