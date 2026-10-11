# Customer Rewards Program

A Spring Boot REST API for calculating customer reward points based on retail transaction purchases.

## Rewards Logic
- **2 points** for every dollar spent **over $100** per transaction.
- **1 point** for every dollar spent **between $50 and $100** per transaction.
- **0 points** for transactions of **$50 or less**.

*Example: A $120 purchase earns `(2 * $20) + (1 * $50) = 90` points.*

## Tech Stack
- Java 17
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Validation)
- In-memory H2 Database
- JUnit 5 & MockMvc

## Key Design Choices
- **Dynamic Monthly Aggregation**: Transactions are grouped dynamically using `YearMonth` without hardcoded month lists, supporting any date range across years.
- **Preloaded Test Data**: Embedded H2 database is initialized on startup via `schema.sql` and `data.sql` (includes 4 customers with multi-month transactions and edge cases like zero transactions).
- **Validation & Error Handling**: Request payloads and query params are validated with Bean Validation; errors return structured JSON with appropriate HTTP status codes (400, 404, 500).
- **Code Standards**: Class and method level JavaDocs explain purpose; zero wildcard imports.

## Getting Started

### Prerequisites
- JDK 17 or higher installed

### Run the Application
```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

### Run Tests
```bash
./mvnw test
```
Runs all 36 unit and integration tests (reward calculation tiers, service layer, and controller MockMvc tests).

### H2 Console
Accessible during runtime at `http://localhost:8080/h2-console`:
- **JDBC URL**: `jdbc:h2:mem:rewardsdb`
- **Username**: `sa`
- **Password**: *(leave blank)*

## API Endpoints

### 1. Get Rewards Summary for All Customers
`GET /api/v1/rewards/customers`

Returns monthly reward breakdown and grand total for each customer. Supports optional `startDate` and `endDate` query parameters (`YYYY-MM-DD`).

```bash
curl http://localhost:8080/api/v1/rewards/customers
```

<details>
<summary>Sample Response</summary>

```json
{
  "success": true,
  "message": "Customer rewards summaries calculated successfully",
  "data": [
    {
      "customerId": 1,
      "customerName": "Alice Johnson",
      "customerEmail": "alice.johnson@example.com",
      "monthlyRewards": [
        {
          "yearMonth": "2026-07",
          "monthName": "JULY",
          "year": 2026,
          "transactionCount": 2,
          "totalSpent": 165.00,
          "rewardPoints": 90
        },
        {
          "yearMonth": "2026-08",
          "monthName": "AUGUST",
          "year": 2026,
          "transactionCount": 2,
          "totalSpent": 275.00,
          "rewardPoints": 275
        },
        {
          "yearMonth": "2026-09",
          "monthName": "SEPTEMBER",
          "year": 2026,
          "transactionCount": 2,
          "totalSpent": 150.00,
          "rewardPoints": 50
        }
      ],
      "totalRewardPoints": 415,
      "totalAmountSpent": 590.00,
      "totalTransactions": 6
    }
  ],
  "timestamp": "2026-10-12T02:00:00.000Z"
}
```
</details>

### 2. Get Rewards Summary for a Specific Customer
`GET /api/v1/rewards/customers/{customerId}`

```bash
curl "http://localhost:8080/api/v1/rewards/customers/1?startDate=2026-07-01&endDate=2026-09-30"
```

### 3. Record a New Transaction
`POST /api/v1/transactions`

Calculates reward points in real time and saves the transaction.

```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "amount": 120.00,
    "transactionDate": "2026-09-25",
    "description": "Store Purchase"
  }'
```

### 4. Customer Endpoints
- `GET /api/v1/customers` - List all customers
- `GET /api/v1/customers/{id}` - Get customer by ID

## Project Structure
```text
src/
├── main/
│   ├── java/com/retailer/rewards/
│   │   ├── controller/      # REST API Controllers (Rewards, Transactions, Customers)
│   │   ├── dto/             # Request & Response records
│   │   ├── entity/          # JPA Entities (Customer, Transaction)
│   │   ├── exception/       # Custom exceptions & global @RestControllerAdvice
│   │   ├── repository/      # Spring Data JPA repositories
│   │   └── service/         # Rewards calculation & business logic
│   └── resources/
│       ├── application.yml  # Application settings
│       ├── schema.sql       # H2 schema DDL
│       └── data.sql         # Preloaded sample dataset
└── test/                    # 36 unit and integration tests
```
