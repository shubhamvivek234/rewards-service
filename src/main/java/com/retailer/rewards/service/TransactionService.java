package com.retailer.rewards.service;

import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.TransactionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionResponseDto recordTransaction(CreateTransactionRequest request);

    List<TransactionResponseDto> getTransactions(Long customerId, LocalDate startDate, LocalDate endDate);

    CustomerRewardsResponse getRewardsSummaryForCustomer(Long customerId, LocalDate startDate, LocalDate endDate);

    List<CustomerRewardsResponse> getRewardsSummaryForAllCustomers(LocalDate startDate, LocalDate endDate);
}
