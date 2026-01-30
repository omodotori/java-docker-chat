# Java Docker Chat Server

A simple, modern real-time chat server built with **Spring Boot** (WebSocket) and **Docker**.

## Features

- **Real-time Messaging**: Uses WebSocket (STOMP) for instant communication.
- **Modern UI**: Clean, responsive web interface using Bootstrap 5.
- **Message Timestamps**: See exactly when messages were sent.
- **Message Persistence**: All messages are saved to PostgreSQL database.
- **Dockerized**: Easy to deploy with Docker and Docker Compose. No local Java installation required!

## Tech Stack

| Technology        | Version | Purpose                 |
| ----------------- | ------- | ----------------------- |
| Java              | 17      | Programming Language    |
| Spring Boot       | 3.2.1   | Backend Framework       |
| WebSocket (STOMP) | -       | Real-time Communication |
| PostgreSQL        | 15      | Database                |
| Docker            | -       | Containerization        |
| JUnit 5           | 5.10.1  | Unit Testing            |
| Mockito           | 5.7.0   | Mocking Framework       |

## Project Structure

```
java/
├── src/
│   ├── main/
│   │   ├── java/com/example/chat/
│   │   │   ├── ChatApplication.java          # Main Spring Boot Application
│   │   │   ├── config/
│   │   │   │   └── WebSocketConfig.java      # WebSocket STOMP Configuration
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java       # Message handling endpoints
│   │   │   │   └── WebSocketEventListener.java # Connection events handler
│   │   │   ├── model/
│   │   │   │   └── ChatMessage.java          # JPA Entity for messages
│   │   │   └── repository/
│   │   │       └── ChatMessageRepository.java # JPA Repository
│   │   └── resources/
│   │       ├── application.properties        # App configuration
│   │       └── static/
│   │           └── index.html                # Frontend client
│   └── test/
│       └── java/com/example/chat/
│           ├── model/
│           │   └── ChatMessageTest.java      # Unit tests for ChatMessage
│           └── controller/
│               └── ChatControllerTest.java   # Unit tests for ChatController
├── TEST_CASES.md                              # Functional test cases documentation
├── Dockerfile                                 # Multi-stage build configuration
├── docker-compose.yml                         # Docker orchestration
└── pom.xml                                    # Maven configuration
```

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed.

## How to Run

### Option 1: Using Docker Compose (Recommended)

This is the easiest way. It builds the project and starts the server automatically.

1. Open your terminal/command prompt in the project folder.
2. Run:
   ```bash
   docker-compose up --build
   ```
3. Wait for the logs to say `Started ChatApplication`.
4. Open [http://localhost:8080/index.html](http://localhost:8080/index.html) in your browser.

To stop the server, press `Ctrl+C` in the terminal.

### Option 2: Using standard Docker

1. Build the image:
   ```bash
   docker build -t java-chat-server .
   ```
2. Run the container:
   ```bash
   docker run -p 8080:8080 java-chat-server
   ```

---

## Testing

### Functional Test Cases

The project includes **10 functional test cases** documented in [TEST_CASES.md](./TEST_CASES.md):

| ID    | Test Case                         | Priority |
| ----- | --------------------------------- | -------- |
| TC-01 | User Connection to Chat           | High     |
| TC-02 | Send Text Message                 | High     |
| TC-03 | Receive Message from Another User | High     |
| TC-04 | User Disconnect from Chat         | Medium   |
| TC-05 | Message Persistence in PostgreSQL | High     |
| TC-06 | Empty Username Validation         | Medium   |
| TC-07 | Empty Message Validation          | Low      |
| TC-08 | WebSocket Reconnection            | Medium   |
| TC-09 | Docker Compose Deployment         | High     |
| TC-10 | Message Timestamp Display         | Low      |

### Unit Tests

The project includes **20 unit tests** covering the core functionality:

#### ChatMessage Entity Tests (10 tests)

Location: `src/test/java/com/example/chat/model/ChatMessageTest.java`

| ID       | Test Description                                |
| -------- | ----------------------------------------------- |
| TC-UT-01 | Builder creates valid message                   |
| TC-UT-02 | prePersist sets timestamp if null               |
| TC-UT-03 | prePersist does not override existing timestamp |
| TC-UT-04 | MessageType enum contains CHAT, JOIN, LEAVE     |
| TC-UT-05 | Setters and getters work correctly              |
| TC-UT-06 | Null content allowed                            |
| TC-UT-07 | Empty sender handling                           |
| TC-UT-08 | AllArgsConstructor works                        |
| TC-UT-09 | NoArgsConstructor creates empty object          |
| TC-UT-10 | Long content handling                           |

#### ChatController Tests (10 tests)

Location: `src/test/java/com/example/chat/controller/ChatControllerTest.java`

| ID       | Test Description                       |
| -------- | -------------------------------------- |
| TC-UT-01 | sendMessage saves and returns message  |
| TC-UT-02 | addUser stores username in session     |
| TC-UT-03 | sendMessage with null content          |
| TC-UT-04 | addUser saves JOIN message to database |
| TC-UT-05 | sendMessage returns same instance      |
| TC-UT-06 | addUser returns message for broadcast  |
| TC-UT-07 | sendMessage with empty sender          |
| TC-UT-08 | Multiple sendMessage calls             |
| TC-UT-09 | addUser preserves message type         |
| TC-UT-10 | sendMessage with long content          |

### Running Tests

**Using Docker (no local Maven required):**

```bash
docker run --rm -v "${PWD}:/app" -w /app maven:3-openjdk-17 mvn test
```

**Using local Maven (if installed):**

```bash
mvn test
```

**Expected output:**

```
Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## API Endpoints

### WebSocket Endpoints

| Endpoint                | Direction       | Description                            |
| ----------------------- | --------------- | -------------------------------------- |
| `/ws`                   | Connect         | WebSocket connection endpoint (SockJS) |
| `/app/chat.sendMessage` | Client → Server | Send a chat message                    |
| `/app/chat.addUser`     | Client → Server | Register user in chat                  |
| `/topic/public`         | Server → Client | Receive broadcasted messages           |

### Message Types

```java
public enum MessageType {
    CHAT,   // Regular chat message
    JOIN,   // User joined notification
    LEAVE   // User left notification
}
```

---

## Database Schema

### chat_messages Table

| Column    | Type        | Description                      |
| --------- | ----------- | -------------------------------- |
| id        | BIGINT (PK) | Auto-generated ID                |
| type      | VARCHAR     | Message type (CHAT, JOIN, LEAVE) |
| content   | VARCHAR     | Message content                  |
| sender    | VARCHAR     | Username of sender               |
| timestamp | TIMESTAMP   | Message creation time            |

---

## Troubleshooting

- **Port 8080 already in use**: Stop any other services running on port 8080 or change the mapping in `docker-compose.yml` (e.g., `"8081:8080"`).
- **Build fails**: Ensure you have a stable internet connection for downloading Maven dependencies on the first run.
- **Tests fail**: Make sure you're running tests with Docker or have Maven + JDK 17 installed locally.

---

## Author

**AITU Student Project** - Java Docker Chat Application with WebSocket

## License

This project is for educational purposes.
