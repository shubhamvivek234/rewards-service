package com.retailer.rewards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a registered retail customer in the rewards system.
 */
@Entity
@Table(name = "customers")
public class Customer {

    /**
     * Unique identifier for the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the customer.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Unique email address of the customer.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Primary phone number of the customer.
     */
    @Column(length = 20)
    private String phone;

    /**
     * Timestamp when the customer record was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Customer() {
    }

    /**
     * Full constructor for creating a customer instance.
     *
     * @param id customer identifier
     * @param name customer full name
     * @param email customer email address
     * @param phone customer phone number
     * @param createdAt timestamp of record creation
     */
    public Customer(Long id, String name, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    /**
     * Gets the customer identifier.
     *
     * @return unique customer ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the customer identifier.
     *
     * @param id unique customer ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer name.
     *
     * @return customer full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer name.
     *
     * @param name customer full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the customer email.
     *
     * @return customer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer email.
     *
     * @param email customer email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the customer phone number.
     *
     * @return customer phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer phone number.
     *
     * @param phone customer phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the record creation timestamp.
     *
     * @return created timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the record creation timestamp.
     *
     * @param createdAt created timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
