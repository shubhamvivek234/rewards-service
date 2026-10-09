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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TransactionService} managing transactions and calculating rewards.
 *
 * <p>Months are grouped and processed dynamically using {@link YearMonth} extracted
 * from each recorded transaction date, ensuring zero hardcoding of months.</p>
 */
@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardsCalculatorService rewardsCalculatorService;

    /**
     * Constructs service with required dependencies.
     *
     * @param transactionRepository transaction data repository
     * @param customerRepository customer data repository
     * @param rewardsCalculatorService calculator service for reward points
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
     * Records a new customer purchase transaction, computes points, and stores the record.
     *
     * @param request transaction creation request
     * @return response DTO for the created transaction
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
     * Retrieves transactions based on optional customer and date range criteria.
     *
     * @param customerId optional customer ID
     * @param startDate optional start date
     * @param endDate optional end date
     * @return list of matching transactions
     */
    @Override
    public List<TransactionResponseDto> getTransactions(Long customerId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        List<Transaction> transactions;

        if (customerId != null) {
            if (!customerRepository.existsById(customerId)) {
                throw CustomerNotFoundException.forId(customerId);
            }
            if (startDate != null && endDate != null) {
                transactions = transactionRepository.findByCustomerIdAndTransactionDateBetweenOrderByTransactionDateAsc(
                        customerId, startDate, endDate);
            } else {
                transactions = transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customerId);
            }
        } else {
            if (startDate != null && endDate != null) {
                transactions = transactionRepository.findByTransactionDateBetweenOrderByTransactionDateAsc(startDate, endDate);
            } else {
                transactions = transactionRepository.findAllWithCustomer();
            }
        }

        return transactions.stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Calculates monthly and total reward points for a single customer.
     *
     * <p>Months are dynamically resolved by grouping transactions by their {@link YearMonth}.</p>
     *
     * @param customerId customer identifier
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @return populated {@link CustomerRewardsResponse}
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
     * Calculates monthly and total reward points across all registered customers.
     *
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @return list of summaries for all customers
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
     * Dynamically groups transactions by month and computes monthly & total points.
     *
     * @param customer customer entity
     * @param transactions customer's transactions
     * @return aggregated rewards response
     */
    private CustomerRewardsResponse buildCustomerRewardsResponse(Customer customer, List<Transaction> transactions) {
        // Dynamic grouping by YearMonth (e.g. 2026-07) - NO HARD CODED MONTHS
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
     * Validates that the provided start date is not after the end date.
     *
     * @param startDate start date
     * @param endDate end date
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidTransactionException("Start date (" + startDate + ") cannot be after end date (" + endDate + ")");
        }
    }

    /**
     * Maps a {@link Transaction} entity to its {@link TransactionResponseDto}.
     *
     * @param transaction entity
     * @return mapped DTO
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
