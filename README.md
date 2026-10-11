# Retailer Customer Rewards Program — Spring Boot REST API
**Charter Code Assignment Solution**

A production-grade Spring Boot REST API application designed to calculate reward points earned by customers for their retail purchase transactions. Includes dynamic monthly grouping (no hard-coded months), H2 embedded database initialized via SQL scripts, thorough unit and integration test coverage for positive and negative scenarios, concise JavaDocs on all classes and methods, and zero wildcard imports.

---

## 📋 Table of Contents
1. [Problem Statement & Rewards Rules](#-problem-statement--rewards-rules)
2. [Key Architecture & Design Highlights](#-key-architecture--design-highlights)
3. [Project Directory Structure](#-project-directory-structure)
4. [Database & Pre-populated SQL Data Set](#-database--pre-populated-sql-data-set)
5. [Prerequisites & Build Tools](#-prerequisites--build-tools)
6. [How to Import and Run in an IDE](#-how-to-import-and-run-in-an-ide)
   - [IntelliJ IDEA](#1-intellij-idea)
   - [Visual Studio Code](#2-visual-studio-code)
   - [Eclipse / STS](#3-eclipse--spring-tool-suite-sts)
7. [Running from Terminal (CLI)](#-running-from-terminal-cli)
8. [REST API Documentation & Sample Requests](#-rest-api-documentation--sample-requests)
   - [Customer Rewards Endpoints](#1-customer-rewards-endpoints)
   - [Transaction Endpoints](#2-transaction-endpoints)
   - [Customer Profile Endpoints](#3-customer-profile-endpoints)
   - [Negative Scenarios & Error Responses](#4-negative-scenarios--error-responses)
9. [Automated Testing & Test Suite Breakdown](#-automated-testing--test-suite-breakdown)
10. [Submission Guidelines Checklist](#-submission-guidelines-checklist)

---

## 🎯 Problem Statement & Rewards Rules

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase:
* **2 points** for every dollar spent **over $100** in each transaction.
* **1 point** for every dollar spent **between $50 and $100** in each transaction.
* Transactions of **$50 or less** earn **0 points**.

### Mathematical Tier Breakdown:
$$\text{Reward Points} = \begin{cases} 
0, & \text{if } \text{amount} \le \$50 \\
\lfloor \text{amount} - 50 \rfloor, & \text{if } \$50 < \text{amount} \le \$100 \\
50 + 2 \times \lfloor \text{amount} - 100 \rfloor, & \text{if } \text{amount} > \$100
\end{cases}$$

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
  * Below / equal to first threshold: **0 points**

Given a record of every transaction during a three-month period (or any custom date range), the service calculates the reward points earned for each customer **per month** and **total**.

---

## 💡 Key Architecture & Design Highlights

1. **No Hard-Coded Months**:
   - The application does **not** hard-code month names (`JULY`, `AUGUST`, `SEPTEMBER`) or calendar indices.
   - Monthly grouping is computed dynamically using `java.time.YearMonth.from(transaction.getTransactionDate())`.
   - The solution seamlessly handles any multi-month window across past, present, or future calendar years without code adjustments.
2. **Database Initialization via SQL Scripts**:
   - No data is hard-coded in Java memory collections.
   - Pre-populated via standard [`src/main/resources/schema.sql`](src/main/resources/schema.sql) (DDL) and [`src/main/resources/data.sql`](src/main/resources/data.sql) (DML) executing upon startup in an embedded in-memory H2 database.
3. **Layered Clean Architecture**:
   - **Controller Layer**: REST controllers validating incoming payloads with Jakarta Bean Validation (`@Valid`).
   - **Service Layer**: Business logic for reward calculations, date validation, and dynamic month aggregation.
   - **Repository Layer**: Spring Data JPA repositories with indexed chronological queries.
   - **Exception Layer**: `@RestControllerAdvice` converting exceptions into standardized, structured JSON error responses.
4. **Code Quality & Submission Constraints**:
   - **Zero wildcard imports**: Every single import is explicitly named (`import java.util.List;` instead of `import java.util.*;`).
   - **Precise JavaDocs**: Clear, concise class-level and method-level JavaDocs on all application classes, interfaces, records, and endpoints.
   - **Target / Bin excluded**: Comprehensive `.gitignore` ensures build artifacts and IDE metadata are never committed.

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
│   │   │   │   ├── CustomerController.java        # Customer lookup endpoints
│   │   │   │   ├── RewardsController.java         # Rewards calculation endpoints
│   │   │   │   └── TransactionController.java     # Transaction recording endpoint
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
│   │   │   │   └── InvalidTransactionException.java # 400 Bad Request exception
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
│           ├── RewardsApplicationTests.java       # Application context loading verification
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

## 🗄️ Database & Pre-populated SQL Data Set

The embedded H2 database automatically runs [`schema.sql`](src/main/resources/schema.sql) and [`data.sql`](src/main/resources/data.sql) on startup.

### Preloaded Customers:
1. **Alice Johnson** (`id=1`): 6 transactions across July, August, September 2026.
   - July 2026: \$120 (90 pts) + \$45 (0 pts) = **90 points**
   - August 2026: \$200 (250 pts) + \$75 (25 pts) = **275 points**
   - September 2026: \$100 (50 pts) + \$50 (0 pts) = **50 points**
   - **Total**: **415 points** (\$590.00 spent across 6 transactions)
2. **Bob Smith** (`id=2`): 6 transactions across July, August, September 2026.
   - July 2026: \$90 (40 pts) + \$150 (150 pts) = **190 points**
   - August 2026: \$130 (110 pts) + \$30 (0 pts) = **110 points**
   - September 2026: \$60 (10 pts) + \$220 (290 pts) = **300 points**
   - **Total**: **600 points** (\$680.00 spent across 6 transactions)
3. **Carol Davis** (`id=3`): 3 transactions across July, August, September 2026.
   - July 2026: \$300 (450 pts) = **450 points**
   - August 2026: \$50 (0 pts) = **0 points**
   - September 2026: \$110 (70 pts) = **70 points**
   - **Total**: **520 points** (\$460.00 spent across 3 transactions)
4. **David Wilson** (`id=4`): **Edge case** — Registered customer with 0 transactions. Correctly returns **0 points** and an empty monthly rewards list without null pointer exceptions.

### H2 Database Console:
When the application is running, the interactive web console is accessible at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:rewardsdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- Username: `sa`
- Password: *(leave blank)*

---

## 🔧 Prerequisites & Build Tools

* **Java JDK**: Version 17 or higher (tested on Java 17, 20, 21).
* **Build Tool**: Self-contained Maven Wrapper (`./mvnw`). No external Maven installation is required.

---

## 🖥️ How to Import and Run in an IDE

### 1. IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Select **File** → **Open...**
3. Choose the project root directory.
4. IntelliJ will detect `pom.xml` and resolve all dependencies automatically.
5. In the Project Explorer, open:
   `src/main/java/com/retailer/rewards/RewardsApplication.java`
6. Click the green **Run ▶** icon next to the `main` method.

### 2. Visual Studio Code
1. Open the project folder in VS Code:
   ```bash
   code /path/to/project
   ```
2. When prompted, install the recommended **Extension Pack for Java**.
3. Press **F5** to start the application with debugging (pre-configured in `.vscode/launch.json`).
4. To run tests, open the Testing tab in the sidebar or run `./mvnw test` from the terminal.

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
Generated HTML documentation will be available in `target/reports/apidocs/index.html`.

---

## 📡 REST API Documentation & Sample Requests

Base URL: `http://localhost:8080`

### 1. Customer Rewards Endpoints

#### A. Get Rewards Summary for All Customers
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
  "timestamp": "2026-10-12T02:00:00.000Z"
}
```

#### B. Get Rewards Summary for a Specific Customer
```bash
curl -X GET http://localhost:8080/api/v1/rewards/customers/1
```
**Optional Date Range Filters:**
```bash
curl -X GET "http://localhost:8080/api/v1/rewards/customers/1?startDate=2026-08-01&endDate=2026-09-30"
```

---

### 2. Transaction Endpoints

#### Record a New Transaction (Real-Time Point Calculation)
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
    "createdAt": "2026-10-12T02:05:00.000"
  },
  "timestamp": "2026-10-12T02:05:00.000Z"
}
```

---

### 3. Customer Profile Endpoints

#### A. List All Customers
```bash
curl -X GET http://localhost:8080/api/v1/customers
```

#### B. Get Customer by ID
```bash
curl -X GET http://localhost:8080/api/v1/customers/1
```

---

### 4. Negative Scenarios & Error Responses

#### A. Customer Not Found (HTTP 404):
```bash
curl -X GET http://localhost:8080/api/v1/rewards/customers/999
```
**Response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with ID: 999",
  "details": [],
  "path": "/api/v1/rewards/customers/999",
  "timestamp": "2026-10-12T02:06:00.000Z"
}
```

#### B. Invalid Transaction Amount (HTTP 400):
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
**Response:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Input validation failed for request payload",
  "details": [
    "amount: Purchase amount must be greater than zero"
  ],
  "path": "/api/v1/transactions",
  "timestamp": "2026-10-12T02:06:05.000Z"
}
```

#### C. Invalid Date Range Filter (HTTP 400):
```bash
curl -X GET "http://localhost:8080/api/v1/rewards/customers/1?startDate=2026-09-30&endDate=2026-07-01"
```
**Response:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Start date (2026-09-30) cannot be after end date (2026-07-01)",
  "details": [],
  "path": "/api/v1/rewards/customers/1",
  "timestamp": "2026-10-12T02:06:10.000Z"
}
```

---

## 🧪 Automated Testing & Test Suite Breakdown

The test suite contains **36 automated unit and integration tests** passing with **0 failures and 0 errors**:

| Test Class | Focus & Scenarios | Tests | Status |
|:---|:---|:---:|:---:|
| [`RewardsCalculatorServiceTest`](src/test/java/com/retailer/rewards/service/RewardsCalculatorServiceTest.java) | Algorithm unit tests across all dollar tiers ($0, $49.99, $50, $51, $75, $90, $100, $101, $120, $200, $250, $300), null values, negative values, and fractional cents | 19 | ✅ Passed |
| [`TransactionServiceTest`](src/test/java/com/retailer/rewards/service/TransactionServiceTest.java) | Dynamic monthly grouping, customer with zero transactions edge case, negative validations (future dates, negative amounts, invalid date ranges) | 7 | ✅ Passed |
| [`RewardsControllerTest`](src/test/java/com/retailer/rewards/controller/RewardsControllerTest.java) | End-to-end `MockMvc` integration tests verifying multi-customer 3-month breakdowns from H2 database, 404 Customer Not Found, and 400 Invalid Date Range | 5 | ✅ Passed |
| [`TransactionControllerTest`](src/test/java/com/retailer/rewards/controller/TransactionControllerTest.java) | End-to-end `MockMvc` integration tests for recording transactions, Bean Validation constraint failures (null customer, negative amount) | 4 | ✅ Passed |
| [`RewardsApplicationTests`](src/test/java/com/retailer/rewards/RewardsApplicationTests.java) | Spring Boot application context loading verification | 1 | ✅ Passed |
| **Total** | | **36** | **100% Passed** |

Execute the full suite anytime via:
```bash
./mvnw clean test
```

---

## ✅ Submission Guidelines Checklist

| Requirement | Implementation Detail | Status |
|:---|:---|:---:|
| **Spring Boot Framework** | Built on Spring Boot with Java 17 baseline | ✅ Completed |
| **RESTful Endpoints** | Exposes `/api/v1/rewards/customers`, `/api/v1/rewards/customers/{id}`, and `/api/v1/transactions` | ✅ Completed |
| **Do Not Hardcode Months** | Computed dynamically using `YearMonth.from(date)` without hardcoded month arrays or strings | ✅ Completed |
| **Unit & Integration Tests** | 36 automated tests across algorithm edge cases, services, and `MockMvc` controllers | ✅ Completed |
| **Negative Scenarios Covered** | 404 Not Found, 400 Bad Request, inverted date ranges, future dates, negative purchase amounts | ✅ Completed |
| **Comprehensive README.md** | Complete architecture, API requests, cURL examples, directory tree, and setup guides | ✅ Completed |
| **Exclude target / bin Folders** | `.gitignore` configured; zero build or IDE artifacts in Git repository | ✅ Completed |
| **Direct Git Repository** | Managed directly in Git, no zip files | ✅ Completed |
| **Class & Method JavaDocs** | Concise, professional JavaDocs on every class, interface, method, and constructor | ✅ Completed |
| **Code Formatting & Cleanliness** | Clean code without clutter or dead code; `recordTransaction` retained at end of service | ✅ Completed |
| **Pre-populated via SQL** | [`schema.sql`](src/main/resources/schema.sql) and [`data.sql`](src/main/resources/data.sql) initialize in-memory H2 DB | ✅ Completed |
| **Zero Wildcard Imports** | All imports explicitly declared; `grep -rn "\.\*;" src/` returns 0 results | ✅ Completed |
