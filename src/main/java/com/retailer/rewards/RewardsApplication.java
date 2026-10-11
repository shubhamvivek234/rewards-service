package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Customer Rewards Program Spring Boot application.
 */
@SpringBootApplication
public class RewardsApplication {

    /**
     * Bootstraps and launches the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }
}
