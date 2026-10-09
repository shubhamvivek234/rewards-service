package com.retailer.rewards.controller;

import com.retailer.rewards.dto.ApiResponse;
import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.TransactionResponseDto;
import com.retailer.rewards.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for customer purchase transactions.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructs controller with transaction service.
     *
     * @param transactionService transaction service
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Lists purchase transactions with optional filtering.
     *
     * @param customerId optional customer identifier filter
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @return list of transactions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getTransactions(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<TransactionResponseDto> transactions = transactionService.getTransactions(customerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Retrieved " + transactions.size() + " transactions", transactions));
    }

    /**
     * Records a new transaction and automatically calculates and stores earned reward points.
     *
     * @param request transaction creation request payload
     * @return created transaction with points
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
