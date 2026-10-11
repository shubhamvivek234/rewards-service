package com.retailer.rewards.controller;

import com.retailer.rewards.dto.ApiResponse;
import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for customer reward calculation and summary endpoints.
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final TransactionService transactionService;

    /**
     * Constructs the rewards controller with transaction service dependency.
     *
     * @param transactionService transaction service
     */
    public RewardsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves rewards summary for all customers, optionally filtered by date range.
     *
     * @param startDate optional ISO start date
     * @param endDate   optional ISO end date
     * @return list of customer reward summaries wrapped in API response
     */
    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<CustomerRewardsResponse>>> getAllCustomerRewards(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<CustomerRewardsResponse> summaries = transactionService.getRewardsSummaryForAllCustomers(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Customer rewards summaries calculated successfully", summaries));
    }

    /**
     * Retrieves rewards summary for a specific customer, optionally filtered by date range.
     *
     * @param customerId customer identifier
     * @param startDate  optional ISO start date
     * @param endDate    optional ISO end date
     * @return customer rewards summary wrapped in API response
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponse<CustomerRewardsResponse>> getCustomerRewards(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        CustomerRewardsResponse summary = transactionService.getRewardsSummaryForCustomer(customerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Customer rewards calculated successfully", summary));
    }
}
