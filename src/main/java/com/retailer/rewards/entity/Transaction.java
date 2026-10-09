package com.retailer.rewards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a recorded purchase transaction made by a customer.
 *
 * <p>Contains transaction metadata including amount, transaction date,
 * and pre-calculated/persisted reward points earned for this transaction.</p>
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated customer who made the purchase.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Monetary purchase amount in USD.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Date on which the purchase transaction occurred.
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    /**
     * Optional brief description of items purchased.
     */
    @Column(length = 255)
    private String description;

    /**
     * Reward points awarded for this transaction.
     */
    @Column(name = "reward_points", nullable = false)
    private Long rewardPoints;

    /**
     * Timestamp when this transaction record was created in the database.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Transaction() {
    }

    /**
     * Full constructor for creating a transaction instance.
     *
     * @param id transaction ID
     * @param customer customer who made the purchase
     * @param amount monetary transaction amount
     * @param transactionDate date the transaction occurred
     * @param description brief description of items purchased
     * @param rewardPoints points earned for this purchase
     * @param createdAt timestamp of record creation
     */
    public Transaction(Long id, Customer customer, BigDecimal amount, LocalDate transactionDate,
                       String description, Long rewardPoints, LocalDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.rewardPoints = rewardPoints;
        this.createdAt = createdAt;
    }

    /**
     * Gets the transaction ID.
     *
     * @return transaction identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the transaction ID.
     *
     * @param id transaction identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer associated with this transaction.
     *
     * @return customer entity
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer associated with this transaction.
     *
     * @param customer customer entity
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the purchase amount.
     *
     * @return transaction monetary amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the purchase amount.
     *
     * @param amount transaction monetary amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the date of the transaction.
     *
     * @return transaction date
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param transactionDate transaction date
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return purchase description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description purchase description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the reward points awarded for this transaction.
     *
     * @return reward points
     */
    public Long getRewardPoints() {
        return rewardPoints;
    }

    /**
     * Sets the reward points awarded for this transaction.
     *
     * @param rewardPoints reward points
     */
    public void setRewardPoints(Long rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", description='" + description + '\'' +
                ", rewardPoints=" + rewardPoints +
                ", createdAt=" + createdAt +
                '}';
    }
}
