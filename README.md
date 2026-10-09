# Starter API — Spring Boot REST Application

A production-ready Spring Boot Java REST API project pre-configured with Maven wrapper, clean layered architecture, validation, comprehensive unit & integration test suite, and IDE configs for IntelliJ IDEA, VS Code, and Eclipse.

---

## 📁 Project Structure

```text
java-starter-project/
├── .mvn/wrapper/                  # Maven wrapper binaries & properties
├── .vscode/                       # VS Code launch and task configs
│   ├── launch.json                # F5 debug launcher
│   ├── tasks.json                 # Build, test, package tasks
│   ├── settings.json              # Java & Maven IDE settings
│   └── extensions.json            # Recommended Java extensions
├── src/
│   ├── main/
│   │   ├── java/com/example/starter/
│   │   │   ├── StarterApiApplication.java    # Spring Boot main entry point
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java            # CORS configuration for frontend clients
│   │   │   ├── controller/
│   │   │   │   ├── HealthController.java     # /api/v1/health endpoint
│   │   │   │   └── TaskController.java       # /api/v1/tasks CRUD REST endpoints
│   │   │   ├── exception/
│   │   │   │   ├── ErrorResponse.java        # Standardized error payload
│   │   │   │   ├── GlobalExceptionHandler.java # @RestControllerAdvice handler
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── ApiResponse.java          # Generic JSON response envelope
│   │   │   │   ├── CreateTaskRequest.java    # Jakarta validation DTO
│   │   │   │   ├── Priority.java             # LOW, MEDIUM, HIGH, URGENT
│   │   │   │   ├── TaskItem.java             # Domain Record model
│   │   │   │   ├── TaskStatus.java           # PENDING, IN_PROGRESS, COMPLETED, CANCELLED
│   │   │   │   └── UpdateTaskRequest.java    # DTO
│   │   │   ├── repository/
│   │   │   │   ├── InMemoryTaskRepository.java # Thread-safe concurrent storage
│   │   │   │   └── TaskRepository.java       # Repository interface
│   │   │   └── service/
│   │   │       ├── TaskService.java          # Service interface
│   │   │       └── TaskServiceImpl.java      # Service implementation
│   │   └── resources/
│   │       └── application.yml               # Port 8080, logging & actuator config
│   └── test/
│       └── java/com/example/starter/
│           ├── StarterApiApplicationTests.java # Context loading test
│           ├── controller/
│           │   └── TaskControllerTest.java   # MockMvc REST API integration tests
│           └── service/
│               └── TaskServiceTest.java      # Service layer unit tests
├── .gitignore                     # Git ignore rules for Java, Maven, IDEs
├── mvnw                           # Maven wrapper script (macOS/Linux)
├── mvnw.cmd                       # Maven wrapper script (Windows)
├── pom.xml                        # Maven dependencies & build configuration
└── README.md
```

---

## 🚀 How to Run in an IDE

### 1. IntelliJ IDEA (Ultimate or Community)
1. Open IntelliJ IDEA.
2. Select **File** → **Open...** (or click **Open** on the Welcome screen).
3. Select the `java-starter-project` folder.
4. IntelliJ will automatically detect `pom.xml`, index dependencies, and configure the project SDK.
5. In the Project Explorer, navigate to:
   `src/main/java/com/example/starter/StarterApiApplication.java`
6. Click the green **Run ▶** icon beside `public static void main(String[] args)`.

### 2. Visual Studio Code
1. Open VS Code: `code /Users/shubham/Documents/Workspace/java-starter-project`.
2. When prompted, install the recommended extensions (or install the **Extension Pack for Java**).
3. Open `StarterApiApplication.java` and click **Run** above the `main` method, or simply press **F5** (the pre-configured launch configuration in `.vscode/launch.json` will execute).
4. Run tasks via **Terminal** → **Run Task...** → select `Maven: Build`, `Maven: Test`, or `Maven: Run Application`.

### 3. Eclipse / Spring Tool Suite (STS)
1. Select **File** → **Import...**
2. Choose **Maven** → **Existing Maven Projects**.
3. Browse to the `java-starter-project` directory and click **Finish**.
4. Right-click the project → **Run As** → **Spring Boot App**.

---

## 💻 Running from Terminal (CLI)

You do **not** need Maven installed globally; the included `./mvnw` wrapper handles everything.

### Start the application:
```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

### Run tests:
```bash
./mvnw test
```

### Build runnable JAR:
```bash
./mvnw clean package
java -jar target/starter-api-1.0.0.jar
```

---

## 📡 REST API Reference

Base URL: `http://localhost:8080`

### 1. Health & Status
```bash
curl http://localhost:8080/api/v1/health
```
Response:
```json
{
  "status": "UP",
  "service": "starter-api",
  "version": "1.0.0",
  "framework": "Spring Boot",
  "timestamp": "2026-10-09T11:32:52.357Z"
}
```

### 2. List Tasks (with optional filtering)
```bash
# Get all tasks
curl http://localhost:8080/api/v1/tasks

# Filter by status and priority
curl "http://localhost:8080/api/v1/tasks?status=IN_PROGRESS&priority=URGENT"

# Search keyword in title or description
curl "http://localhost:8080/api/v1/tasks?search=Spring"
```

### 3. Get Task by ID
```bash
curl http://localhost:8080/api/v1/tasks/task-101
```

### 4. Create Task
```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design authentication flow",
    "description": "Evaluate JWT and OAuth2 security patterns",
    "priority": "HIGH"
  }'
```

### 5. Update Task
```bash
curl -X PUT http://localhost:8080/api/v1/tasks/task-101 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Explore Spring Boot project structure (Done)",
    "description": "Reviewed architecture and validated all tests.",
    "status": "COMPLETED",
    "priority": "MEDIUM"
  }'
```

### 6. Update Task Status (Patch)
```bash
curl -X PATCH "http://localhost:8080/api/v1/tasks/task-102/status?status=COMPLETED"
```

### 7. Delete Task
```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/task-101
```

### 8. Spring Boot Actuator Endpoints
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- App info: `http://localhost:8080/actuator/info`

---

## ⚙️ Configuration

Application settings are located in [src/main/resources/application.yml](src/main/resources/application.yml):
- **Server Port**: `8080` (can be changed to any port or overridden with `SERVER_PORT=9090 ./mvnw spring-boot:run`)
- **CORS**: Configured in [WebConfig.java](src/main/java/com/example/starter/config/WebConfig.java) for `localhost:3000`, `localhost:5173`, and `localhost:8080`.
