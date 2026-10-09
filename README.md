# Retailer Customer Rewards Program — Spring Boot REST API
**Charter Code Assignment Solution**

A production-grade Spring Boot REST API application designed to calculate reward points earned by customers for their retail purchase transactions. Includes dynamic monthly grouping (no hard-coded months), H2 embedded database initialized via SQL scripts, thorough unit and integration test coverage for positive and negative scenarios, comprehensive JavaDocs on all classes and methods, and zero wildcard imports.

---

## 📋 Table of Contents
1. [Problem Statement & Rewards Rules](#-problem-statement--rewards-rules)
2. [Key Architecture & Design Highlights](#-key-architecture--design-highlights)
3. [Project Directory Structure](#-project-directory-structure)
4. [Database & SQL Data Set](#-database--sql-data-set)
5. [Prerequisites & Build Tools](#-prerequisites--build-tools)
6. [How to Import and Run in an IDE](#-how-to-import-and-run-in-an-ide)
   - [IntelliJ IDEA](#1-intellij-idea)
   - [Visual Studio Code](#2-visual-studio-code)
   - [Eclipse / STS](#3-eclipse--spring-tool-suite-sts)
7. [Running from Terminal (CLI)](#-running-from-terminal-cli)
8. [REST API Documentation & Sample Requests](#-rest-api-documentation--sample-requests)
9. [Automated Testing & Coverage](#-automated-testing--coverage)
10. [Submission Guidelines Checklist](#-submission-guidelines-checklist)

---

## 🎯 Problem Statement & Rewards Rules

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase:
* **2 points** for every dollar spent **over $100** in each transaction.
* **1 point** for every dollar spent **between $50 and $100** in each transaction.
* Transactions of **$50 or less** earn **0 points**.

### Example Calculations:
* **$120 purchase**:
  * Over $100: $(120 - 100) \times 2 = 2 \times 20 = 40$ points
  * Between $50 and $100: $50 \times 1 = 50$ points
  * **Total**: $40 + 50 = \mathbf{90\text{ points}}$
* **$200 purchase**:
  * Over $100: $(200 - 100) \times 2 = 2 \times 100 = 200$ points
  * Between $50 and $100: $50 \times 1 = 50$ points
  * **Total**: $200 + 50 = \mathbf{250\text{ points}}$
* **$90 purchase**:
  * Over $100: $0$ points
  * Between $50 and $100: $(90 - 50) \times 1 = 40$ points
  * **Total**: $\mathbf{40\text{ points}}$
* **$50 or $45 purchase**:
  * **Total**: $\mathbf{0\text{ points}}$

Given a record of every transaction during a three-month period (or any flexible range), the service calculates the reward points earned for each customer **per month** and **total**.

---

## 💡 Key Architecture & Design Highlights

1. **No Hard-Coded Months**:
   - The application does **not** hard-code month names (`JULY`, `AUGUST`, `SEPTEMBER`) or numeric indices (`1`, `2`, `3`).
   - Grouping is executed dynamically using `java.time.YearMonth.from(transaction.getTransactionDate())`.
   - The solution seamlessly accommodates any multi-month window across any calendar years (past, present, or future).
2. **Database Initialization via SQL Scripts**:
   - Customer and transaction data are not hard-coded in Java collections.
   - Initialized via standard [`src/main/resources/schema.sql`](src/main/resources/schema.sql) (DDL) and [`src/main/resources/data.sql`](src/main/resources/data.sql) (DML) executing on an embedded in-memory H2 database.
3. **Layered Clean Architecture**:
   - **Controller Layer**: REST controllers validating incoming payloads with Jakarta Bean Validation.
   - **Service Layer**: Business logic for reward point calculation, date validation, and dynamic month aggregation.
   - **Repository Layer**: Spring Data JPA repositories with query optimization to prevent $N+1$ query overhead.
   - **Exception Layer**: `@RestControllerAdvice` converting exceptions into standardized, structured JSON error responses (RFC 7807 compliant).
4. **Strict Coding Standards & JavaDocs**:
   - **Zero wildcard imports**: Every import is explicitly listed.
   - **Class and method level JavaDocs**: Every class, method, interface, and constructor includes documentation.
   - Clean, idiomatic Java 17+ features (Records, Pattern Matching, Stream API).

---

## 📁 Project Directory Structure

```text
rewards-service/
├── .mvn/wrapper/                                  # Maven wrapper binaries & properties
├── .vscode/                                       # Pre-configured VS Code tasks and debug launcher
│   ├── launch.json                                # Press F5 to debug
│   ├── tasks.json                                 # Build, test, package tasks
│   ├── settings.json                              # IDE settings
│   └── extensions.json                            # Recommended extensions
├── src/
│   ├── main/
│   │   ├── java/com/retailer/rewards/
│   │   │   ├── RewardsApplication.java            # Main Spring Boot entry point
│   │   │   ├── controller/
│   │   │   │   ├── CustomerController.java        # Customer retrieval endpoints
│   │   │   │   ├── RewardsController.java         # Rewards calculation endpoints
│   │   │   │   └── TransactionController.java     # Transaction recording and queries
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java               # Standard API response envelope
│   │   │   │   ├── CreateTransactionRequest.java  # Validated transaction creation DTO
│   │   │   │   ├── CustomerRewardsResponse.java   # Customer summary with monthly breakdown
│   │   │   │   ├── MonthlyRewardDto.java          # Dynamic monthly rewards DTO
│   │   │   │   └── TransactionResponseDto.java    # Single transaction response DTO
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java                  # JPA entity for customers table
│   │   │   │   └── Transaction.java               # JPA entity for transactions table
│   │   │   ├── exception/
│   │   │   │   ├── CustomerNotFoundException.java # 404 Customer lookup exception
│   │   │   │   ├── ErrorResponse.java             # Standard error response DTO
│   │   │   │   ├── GlobalExceptionHandler.java    # Centralized @RestControllerAdvice
│   │   │   │   ├── InvalidTransactionException.java # 400 Bad Request exception
│   │   │   │   └── ResourceNotFoundException.java # General 404 exception
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java        # Spring Data JPA Customer repo
│   │   │   │   └── TransactionRepository.java     # Spring Data JPA Transaction repo
│   │   │   └── service/
│   │   │       ├── CustomerService.java           # Customer service interface
│   │   │       ├── CustomerServiceImpl.java       # Customer service implementation
│   │   │       ├── RewardsCalculatorService.java  # Reward formula service interface
│   │   │       ├── RewardsCalculatorServiceImpl.java # Formula implementation
│   │   │       ├── TransactionService.java        # Transaction & rewards interface
│   │   │       └── TransactionServiceImpl.java    # Dynamic monthly aggregation implementation
│   │   └── resources/
│   │       ├── application.yml                    # App configuration (H2, JPA, logging)
│   │       ├── data.sql                           # Multi-customer 3-month SQL dataset
│   │       └── schema.sql                         # H2 DDL table creation script
│   └── test/
│       └── java/com/retailer/rewards/
│           ├── RewardsApplicationTests.java       # Context loading verification
│           ├── controller/
│           │   ├── RewardsControllerTest.java     # MockMvc integration tests (positive/negative)
│           │   └── TransactionControllerTest.java # MockMvc transaction tests (positive/negative)
│           └── service/
│               ├── RewardsCalculatorServiceTest.java # Formula unit tests (19 edge cases)
│               └── TransactionServiceTest.java    # Aggregation & exception unit tests
├── .gitignore                                     # Excludes target/, bin/, IDE metadata
├── mvnw                                           # Maven wrapper script for macOS/Linux
├── mvnw.cmd                                       # Maven wrapper script for Windows
├── pom.xml                                        # Maven project descriptor
└── README.md                                      # Project documentation
```

---

## 🗄️ Database & SQL Data Set

The embedded H2 database automatically runs [`schema.sql`](src/main/resources/schema.sql) and [`data.sql`](src/main/resources/data.sql) on startup.

### Preloaded Customers:
1. **Alice Johnson** (`id=1`): 6 transactions across July, August, September 2026.
   - July 2026: \$120 (90 pts) + \$45 (0 pts) = **90 points**
   - August 2026: \$200 (250 pts) + \$75 (25 pts) = **275 points**
   - September 2026: \$100 (50 pts) + \$50 (0 pts) = **50 points**
   - **Total**: **415 points** (\$590.00 spent)
2. **Bob Smith** (`id=2`): 6 transactions across July, August, September 2026.
   - July 2026: \$90 (40 pts) + \$150 (150 pts) = **190 points**
   - August 2026: \$130 (110 pts) + \$30 (0 pts) = **110 points**
   - September 2026: \$60 (10 pts) + \$220 (290 pts) = **300 points**
   - **Total**: **600 points** (\$680.00 spent)
3. **Carol Davis** (`id=3`): 3 transactions across July, August, September 2026.
   - July 2026: \$300 (450 pts) = **450 points**
   - August 2026: \$50 (0 pts) = **0 points**
   - September 2026: \$110 (70 pts) = **70 points**
   - **Total**: **520 points** (\$460.00 spent)
4. **David Wilson** (`id=4`): **Edge case** — Customer with 0 transactions. Correctly returns **0 points** and an empty monthly list without throwing null pointer errors.

### H2 Database Console:
When the application is running, the interactive web console is accessible at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:rewardsdb`
- Username: `sa`
- Password: *(leave blank)*

---

## 🔧 Prerequisites & Build Tools

* **Java JDK**: Version 17 or higher (tested with Java 17, 20, 21).
* **Build Tool**: Self-contained Maven Wrapper (`./mvnw`). No local Maven installation is required.

---

## 🖥️ How to Import and Run in an IDE

### 1. IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click **File** → **Open...**
3. Select the `rewards-service` project directory.
4. IntelliJ will recognize `pom.xml` and automatically download dependencies.
5. In the Project Explorer, open:
   `src/main/java/com/retailer/rewards/RewardsApplication.java`
6. Click the green **Run ▶** icon beside the `main` method.

### 2. Visual Studio Code
1. Open the project folder in VS Code:
   ```bash
   code /path/to/rewards-service
   ```
2. When prompted, install the recommended **Extension Pack for Java**.
3. Press **F5** to start the application with debugging (pre-configured in `.vscode/launch.json`).
4. To run tests, open the Test view in the sidebar or run `./mvnw test` from the integrated terminal.

### 3. Eclipse / Spring Tool Suite (STS)
1. Select **File** → **Import...**
2. Choose **Maven** → **Existing Maven Projects**.
3. Browse to the project folder and click **Finish**.
4. Right-click the project → **Run As** → **Spring Boot App**.

---

## 💻 Running from Terminal (CLI)

### 1. Start the Spring Boot application:
```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

### 2. Run all unit and integration tests:
```bash
./mvnw test
```

### 3. Build a standalone runnable JAR:
```bash
./mvnw clean package
java -jar target/rewards-service-1.0.0.jar
```

### 4. Generate JavaDoc documentation:
```bash
./mvnw javadoc:javadoc
```
Generated HTML documentation will be in `target/reports/apidocs/index.html`.

---

## 📡 REST API Documentation & Sample Requests

Base URL: `http://localhost:8080`

### 1. Get Rewards Summary for All Customers
Calculates the dynamic monthly breakdown and overall total points for every customer:
```bash
curl -X GET http://localhost:8080/api/v1/rewards/customers
```
**Sample Response (HTTP 200 OK):**
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
  "timestamp": "2026-10-09T11:45:00.000Z"
}
```

---

### 2. Get Rewards Summary for a Specific Customer
```bash
curl -X GET http://localhost:8080/api/v1/rewards/customers/1
```
Optional date filters:
```bash
curl -X GET "http://localhost:8080/api/v1/rewards/customers/1?startDate=2026-08-01&endDate=2026-09-30"
```

---

### 3. Record a New Transaction (Real-time Point Calculation)
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
**Sample Response (HTTP 201 Created):**
```json
{
  "success": true,
  "message": "Transaction recorded successfully",
  "data": {
    "id": 16,
    "customerId": 1,
    "customerName": "Alice Johnson",
    "amount": 120.00,
    "transactionDate": "2026-09-25",
    "description": "Store Purchase",
    "rewardPoints": 90,
    "createdAt": "2026-10-09T11:45:00.000"
  },
  "timestamp": "2026-10-09T11:45:00.000Z"
}
```

---

### 4. Negative Test Scenarios & Error Responses

#### A. Customer Not Found (HTTP 404):
```bash
curl -X GET http://localhost:8080/api/v1/rewards/customers/999
```
Response:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: 999",
  "details": [],
  "path": "/api/v1/rewards/customers/999",
  "timestamp": "2026-10-09T11:45:05.588Z"
}
```

#### B. Invalid Amount Validation (HTTP 400):
```bash
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "amount": -50.00,
    "transactionDate": "2026-09-25",
    "description": "Invalid Refund"
  }'
```
Response:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Input validation failed for request payload",
  "details": ["amount: Purchase amount must be greater than zero"],
  "path": "/api/v1/transactions",
  "timestamp": "2026-10-09T11:45:05.722Z"
}
```

#### C. Invalid Date Range (HTTP 400):
```bash
curl -X GET "http://localhost:8080/api/v1/rewards/customers/1?startDate=2026-09-30&endDate=2026-07-01"
```
Response:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Start date (2026-09-30) cannot be after end date (2026-07-01)",
  "details": [],
  "path": "/api/v1/rewards/customers/1",
  "timestamp": "2026-10-09T11:45:06.000Z"
}
```

---

## 🧪 Automated Testing & Coverage

The test suite contains **38 automated unit and integration tests** passing with **0 failures and 0 errors**:

| Test Class | Focus | Test Count |
|:---|:---|:---:|
| [`RewardsCalculatorServiceTest`](src/test/java/com/retailer/rewards/service/RewardsCalculatorServiceTest.java) | Algorithm unit tests across all dollar tiers ($0, $49.99, $50, $51, $75, $90, $100, $101, $120, $200, $250, $300), null, negative, and fractional cents | 19 |
| [`TransactionServiceTest`](src/test/java/com/retailer/rewards/service/TransactionServiceTest.java) | Dynamic monthly grouping, edge cases (zero transactions), negative validations (future dates, negative amounts, date ranges) | 7 |
| [`RewardsControllerTest`](src/test/java/com/retailer/rewards/controller/RewardsControllerTest.java) | End-to-end MockMvc integration tests verifying customer totals and 3-month breakdowns from H2 SQL data, plus 404 & 400 error handlers | 5 |
| [`TransactionControllerTest`](src/test/java/com/retailer/rewards/controller/TransactionControllerTest.java) | End-to-end MockMvc integration tests for listing, recording transactions, and Bean Validation error responses | 6 |
| [`RewardsApplicationTests`](src/test/java/com/retailer/rewards/RewardsApplicationTests.java) | Application context loading test | 1 |
| **Total** | | **38** |

Run tests at any time using:
```bash
./mvnw test
```

---

## ✅ Submission Guidelines Checklist

- [x] **Solved using Spring Boot** (Spring Boot 4.x / Java 17+ baseline)
- [x] **RESTful endpoints** (`/api/v1/rewards/customers`, `/api/v1/rewards/customers/{id}`, `/api/v1/transactions`)
- [x] **Do not hard code the months** (Dynamic `YearMonth` grouping)
- [x] **Unit and integration tests present** (38 tests covering multiple customers, transactions, and edge cases)
- [x] **Exception and negative test scenarios included** (404, 400, validation errors)
- [x] **Comprehensive README.md** explaining structure, implementation, and run instructions
- [x] **Do not check in target/bin folder** (Clean `.gitignore` configured)
- [x] **Do not upload zip version** (Committed directly to Git repository)
- [x] **Class and method level JavaDocs** explaining purpose
- [x] **Formatted code** following Java coding standards
- [x] **Do not hardcode data, use SQL scripts** (`schema.sql` and `data.sql` preloading multi-month customer dataset)
- [x] **Remove wildcard imports** (Zero `.*` imports in codebase)
