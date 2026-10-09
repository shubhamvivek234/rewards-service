package com.retailer.rewards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Verifies that the Spring Boot ApplicationContext boots cleanly with
 * H2 embedded datasource, JPA entity mappings, and SQL scripts.
 */
@SpringBootTest
class RewardsApplicationTests {

    /**
     * Test case ensuring application context initialization succeeds.
     */
    @Test
    @DisplayName("Application context should load successfully with H2 database and JPA repositories")
    void contextLoads() {
        // Successful startup confirms Spring Data JPA and H2 database configuration
    }
}
