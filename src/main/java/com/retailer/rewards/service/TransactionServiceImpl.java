package com.retailer.rewards.service;

import com.retailer.rewards.dto.CreateTransactionRequest;
import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.MonthlyRewardDto;
import com.retailer.rewards.dto.TransactionResponseDto;
import com.retailer.rewards.entity.Customer;
import com.retailer.rewards.entity.Transaction;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.exception.InvalidTransactionException;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TransactionService} managing customer rewards and transaction persistence.
 */
@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardsCalculatorService rewardsCalculatorService;

    /**
     * Constructs the transaction service with required dependencies.
     *
     * @param transactionRepository  repository for transaction persistence
     * @param customerRepository     repository for customer access
     * @param rewardsCalculatorService calculator service for points computation
     */
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            CustomerRepository customerRepository,
            RewardsCalculatorService rewardsCalculatorService
    ) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.rewardsCalculatorService = rewardsCalculatorService;
    }

    /**
     * Retrieves reward summaries grouped dynamically by month for all customers.
     *
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return list of customer reward responses
     */
    @Override
    public List<CustomerRewardsResponse> getRewardsSummaryForAllCustomers(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        List<Customer> allCustomers = customerRepository.findAll();

        return allCustomers.stream()
                .map(customer -> {
                    List<Transaction> customerTransactions;
                    if (startDate != null && endDate != null) {
                        customerTransactions = transactionRepository.findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
                                customer.getId(), startDate, endDate);
                    } else {
                        customerTransactions = transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customer.getId());
                    }
                    return buildCustomerRewardsResponse(customer, customerTransactions);
                })
                .toList();
    }

    /**
     * Retrieves reward summary grouped dynamically by month for a specific customer.
     *
     * @param customerId customer identifier
     * @param startDate  optional start date filter
     * @param endDate    optional end date filter
     * @return customer rewards response
     */
    @Override
    public CustomerRewardsResponse getRewardsSummaryForCustomer(Long customerId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> CustomerNotFoundException.forId(customerId));

        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
                customerId, startDate, endDate);
        } else {
            transactions = transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customerId);
        }

        return buildCustomerRewardsResponse(customer, transactions);
    }

    /**
     * Validates input, calculates reward points, and persists a new customer transaction.
     *
     * @param request transaction creation request
     * @return persisted transaction response
     */
    @Override
    @Transactional
    public TransactionResponseDto recordTransaction(CreateTransactionRequest request) {
        log.info("Recording new transaction for customer ID: {}, amount: ${}", request.customerId(), request.amount());

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> CustomerNotFoundException.forId(request.customerId()));

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transaction amount must be strictly greater than zero");
        }

        if (request.transactionDate().isAfter(LocalDate.now())) {
            throw new InvalidTransactionException("Transaction date cannot be in the future");
        }

        long calculatedPoints = rewardsCalculatorService.calculatePoints(request.amount());

        Transaction transaction = new Transaction(
                null,
                customer,
                request.amount(),
                request.transactionDate(),
                request.description(),
                calculatedPoints,
                LocalDateTime.now()
        );

        Transaction saved = transactionRepository.save(transaction);
        log.info("Saved transaction ID {} with {} points awarded", saved.getId(), calculatedPoints);

        return toResponseDto(saved);
    }

    /**
     * Aggregates transactions by month and computes monthly and grand total rewards.
     *
     * @param customer     customer entity
     * @param transactions customer transactions
     * @return populated rewards response
     */
    private CustomerRewardsResponse buildCustomerRewardsResponse(Customer customer, List<Transaction> transactions) {
        Map<YearMonth, List<Transaction>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate())));

        List<MonthlyRewardDto> monthlyRewards = transactionsByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    YearMonth ym = entry.getKey();
                    List<Transaction> monthTransactions = entry.getValue();

                    long monthPoints = monthTransactions.stream()
                            .mapToLong(Transaction::getRewardPoints)
                            .sum();

                    BigDecimal monthTotalSpent = monthTransactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new MonthlyRewardDto(
                            ym.toString(),
                            ym.getMonth().name(),
                            ym.getYear(),
                            monthTransactions.size(),
                            monthTotalSpent,
                            monthPoints
                    );
                })
                .toList();

        long grandTotalPoints = monthlyRewards.stream()
                .mapToLong(MonthlyRewardDto::rewardPoints)
                .sum();

        BigDecimal grandTotalSpent = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CustomerRewardsResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                monthlyRewards,
                grandTotalPoints,
                grandTotalSpent,
                transactions.size()
        );
    }

    /**
     * Validates that start date is not after end date when both are provided.
     *
     * @param startDate range start date
     * @param endDate   range end date
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidTransactionException("Start date (" + startDate + ") cannot be after end date (" + endDate + ")");
        }
    }

    /**
     * Maps a {@link Transaction} entity to a {@link TransactionResponseDto}.
     *
     * @param transaction transaction entity
     * @return transaction response DTO
     */
    private TransactionResponseDto toResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getCustomer().getId(),
                transaction.getCustomer().getName(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getRewardPoints(),
                transaction.getCreatedAt()
        );
    }
}
