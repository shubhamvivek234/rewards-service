package com.retailer.rewards.service;

import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.TransactionResponseDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service managing purchase transactions and calculating dynamic reward summaries.
 */
public interface TransactionService {

    /**
     * Records a new customer purchase transaction and calculates its reward points.
     *
     * @param request transaction creation request payload
     * @return response DTO containing saved transaction and earned points
     */
    TransactionResponseDto recordTransaction(CreateTransactionRequest request);

    /**
     * Retrieves recorded transactions filtered optionally by customer and date range.
     *
     * @param customerId optional customer ID filter
     * @param startDate optional inclusive start date filter
     * @param endDate optional inclusive end date filter
     * @return list of transaction DTOs
     */
    List<TransactionResponseDto> getTransactions(Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the monthly and total reward points earned by a specific customer.
     *
     * <p>Months are computed dynamically from transaction dates and are never hard-coded.</p>
     *
     * @param customerId customer unique identifier
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return reward points summary with dynamic monthly breakdown and grand total
     */
    CustomerRewardsResponse getRewardsSummaryForCustomer(Long customerId, LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the monthly and total reward points earned for all registered customers.
     *
     * @param startDate optional inclusive start date
     * @param endDate optional inclusive end date
     * @return list of reward point summaries for all customers
     */
    List<CustomerRewardsResponse> getRewardsSummaryForAllCustomers(LocalDate startDate, LocalDate endDate);
}
