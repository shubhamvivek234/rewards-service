package com.retailer.rewards.service;

import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.TransactionResponseDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing customer transactions and calculating reward summaries.
 */
public interface TransactionService {

    /**
     * Retrieves rewards summary grouped by month for all customers within an optional date range.
     *
     * @param startDate optional start date filter (inclusive)
     * @param endDate   optional end date filter (inclusive)
     * @return list of rewards responses for all customers
     */
    List<CustomerRewardsResponse> getRewardsSummaryForAllCustomers(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves rewards summary grouped by month for a single customer within an optional date range.
     *
     * @param customerId unique ID of the customer
     * @param startDate  optional start date filter (inclusive)
     * @param endDate    optional end date filter (inclusive)
     * @return rewards response for the specified customer
     */
    CustomerRewardsResponse getRewardsSummaryForCustomer(Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * Records a new transaction, calculates reward points, and persists it.
     *
     * @param request transaction creation payload
     * @return recorded transaction response details
     */
    TransactionResponseDto recordTransaction(CreateTransactionRequest request);
}
