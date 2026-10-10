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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    private RewardsCalculatorService rewardsCalculatorService;
    private TransactionService transactionService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        rewardsCalculatorService = new RewardsCalculatorServiceImpl();
        transactionService = new TransactionServiceImpl(
                transactionRepository,
                customerRepository,
                rewardsCalculatorService
        );

        sampleCustomer = new Customer(
                1L,
                "Alice Johnson",
                "alice.johnson@example.com",
                "+1-555-0101",
                LocalDateTime.now()
        );
    }

    @Test
    void testDynamicThreeMonthCalculation() {
        Transaction t1 = new Transaction(1L, sampleCustomer, BigDecimal.valueOf(120.00),
                LocalDate.of(2026, 7, 12), "Electronics", 90L, LocalDateTime.now());
        Transaction t2 = new Transaction(2L, sampleCustomer, BigDecimal.valueOf(45.00),
                LocalDate.of(2026, 7, 25), "Coffee", 0L, LocalDateTime.now());
        Transaction t3 = new Transaction(3L, sampleCustomer, BigDecimal.valueOf(200.00),
                LocalDate.of(2026, 8, 5), "Appliance", 250L, LocalDateTime.now());
        Transaction t4 = new Transaction(4L, sampleCustomer, BigDecimal.valueOf(75.00),
                LocalDate.of(2026, 8, 20), "Clothing", 25L, LocalDateTime.now());
        Transaction t5 = new Transaction(5L, sampleCustomer, BigDecimal.valueOf(100.00),
                LocalDate.of(2026, 9, 2), "Grocery", 50L, LocalDateTime.now());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(List.of(t1, t2, t3, t4, t5));

        CustomerRewardsResponse response = transactionService.getRewardsSummaryForCustomer(1L, null, null);

        assertThat(response.customerId()).isEqualTo(1L);
        assertThat(response.customerName()).isEqualTo("Alice Johnson");
        assertThat(response.totalTransactions()).isEqualTo(5);
        assertThat(response.totalAmountSpent()).isEqualByComparingTo(BigDecimal.valueOf(540.00));
        assertThat(response.totalRewardPoints()).isEqualTo(415L);

        List<MonthlyRewardDto> monthlyRewards = response.monthlyRewards();
        assertThat(monthlyRewards).hasSize(3);

        MonthlyRewardDto july = monthlyRewards.get(0);
        assertThat(july.yearMonth()).isEqualTo("2026-07");
        assertThat(july.monthName()).isEqualTo("JULY");
        assertThat(july.transactionCount()).isEqualTo(2);
        assertThat(july.rewardPoints()).isEqualTo(90L);

        MonthlyRewardDto august = monthlyRewards.get(1);
        assertThat(august.yearMonth()).isEqualTo("2026-08");
        assertThat(august.monthName()).isEqualTo("AUGUST");
        assertThat(august.transactionCount()).isEqualTo(2);
        assertThat(august.rewardPoints()).isEqualTo(275L);

        MonthlyRewardDto september = monthlyRewards.get(2);
        assertThat(september.yearMonth()).isEqualTo("2026-09");
        assertThat(september.monthName()).isEqualTo("SEPTEMBER");
        assertThat(september.transactionCount()).isEqualTo(1);
        assertThat(september.rewardPoints()).isEqualTo(50L);
    }

    @Test
    void testCustomerWithNoTransactions() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L)).thenReturn(List.of());

        CustomerRewardsResponse response = transactionService.getRewardsSummaryForCustomer(1L, null, null);

        assertThat(response.customerId()).isEqualTo(1L);
        assertThat(response.totalTransactions()).isEqualTo(0);
        assertThat(response.totalRewardPoints()).isEqualTo(0L);
        assertThat(response.totalAmountSpent()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.monthlyRewards()).isEmpty();
    }

    @Test
    void testGetRewardsCustomerNotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getRewardsSummaryForCustomer(999L, null, null))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with ID: 999");
    }

    @Test
    void testInvalidDateRangeThrows() {
        LocalDate start = LocalDate.of(2026, 9, 30);
        LocalDate end = LocalDate.of(2026, 7, 1);

        assertThatThrownBy(() -> transactionService.getRewardsSummaryForCustomer(1L, start, end))
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessageContaining("cannot be after end date");
    }

    @Test
    void testRecordTransactionSuccess() {
        CreateTransactionRequest request = new CreateTransactionRequest(
                1L,
                BigDecimal.valueOf(120.00),
                LocalDate.now().minusDays(1),
                "Smartphone purchase"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        TransactionResponseDto response = transactionService.recordTransaction(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.customerId()).isEqualTo(1L);
        assertThat(response.customerName()).isEqualTo("Alice Johnson");
        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.valueOf(120.00));
        assertThat(response.rewardPoints()).isEqualTo(90L);

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testRecordTransactionNegativeAmount() {
        CreateTransactionRequest request = new CreateTransactionRequest(
                1L,
                BigDecimal.valueOf(-10.00),
                LocalDate.now().minusDays(1),
                "Invalid refund"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));

        assertThatThrownBy(() -> transactionService.recordTransaction(request))
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessageContaining("must be strictly greater than zero");
    }

    @Test
    void testRecordTransactionFutureDate() {
        CreateTransactionRequest request = new CreateTransactionRequest(
                1L,
                BigDecimal.valueOf(100.00),
                LocalDate.now().plusDays(5),
                "Future order"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));

        assertThatThrownBy(() -> transactionService.recordTransaction(request))
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessageContaining("cannot be in the future");
    }
}
