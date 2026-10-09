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
 * REST controller exposing endpoints for calculating customer reward points.
 *
 * <p>Endpoints provide dynamic monthly breakdowns and total accumulated reward
 * points based on the recorded purchases.</p>
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final TransactionService transactionService;

    /**
     * Constructs the controller with required transaction service.
     *
     * @param transactionService transaction and rewards service
     */
    public RewardsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves the rewards points summary for all registered customers.
     *
     * <p>Returns monthly reward points breakdown and overall total points for each customer.
     * Months are calculated dynamically and are never hard-coded.</p>
     *
     * @param startDate optional inclusive start date filter (ISO format YYYY-MM-DD)
     * @param endDate optional inclusive end date filter (ISO format YYYY-MM-DD)
     * @return response entity containing list of customer rewards summaries
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
     * Retrieves the rewards points summary for a specific customer.
     *
     * @param customerId unique identifier of the customer
     * @param startDate optional inclusive start date filter (ISO format YYYY-MM-DD)
     * @param endDate optional inclusive end date filter (ISO format YYYY-MM-DD)
     * @return response entity containing customer reward summary
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
