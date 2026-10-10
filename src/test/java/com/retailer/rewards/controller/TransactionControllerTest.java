package com.retailer.rewards.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
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

    @Test
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

    @Test
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

    @Test
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
