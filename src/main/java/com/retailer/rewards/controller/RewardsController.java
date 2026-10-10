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

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardsController {

    private final TransactionService transactionService;

    public RewardsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<CustomerRewardsResponse>>> getAllCustomerRewards(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<CustomerRewardsResponse> summaries = transactionService.getRewardsSummaryForAllCustomers(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Customer rewards summaries calculated successfully", summaries));
    }

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
