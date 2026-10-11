package com.retailer.rewards.controller;

import com.retailer.rewards.dto.ApiResponse;
import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.TransactionResponseDto;
import com.retailer.rewards.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for transaction management endpoints.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructs the transaction controller with service dependency.
     *
     * @param transactionService transaction service
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Records a new transaction and calculates earned reward points.
     *
     * @param request valid transaction creation payload
     * @return created transaction details wrapped in API response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDto>> recordTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        TransactionResponseDto response = transactionService.recordTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Transaction recorded successfully", response));
    }
}
