package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Retailer Customer Rewards Program application.
 *
 * <p>Bootstraps the Spring Boot context, sets up embedded H2 datasource,
 * initializes schema and test data from SQL scripts, and exposes RESTful endpoints
 * for calculating customer reward points.</p>
 */
@SpringBootApplication
public class RewardsApplication {

    /**
     * Default constructor for RewardsApplication.
     */
    public RewardsApplication() {
    }

    /**
     * Application launch method.
     *
     * @param args command-line arguments passed to the JVM
     */
    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }
}
