# Java Docker Chat Server

A simple, modern real-time chat server built with **Spring Boot** (WebSocket) and **Docker**.

## Features

- **Real-time Messaging**: Uses WebSocket (STOMP) for instant communication.
- **Modern UI**: Clean, responsive web interface using Bootstrap 5.
- **Message Timestamps**: See exactly when messages were sent.
- **Dockerized**: Easy to deploy with Docker and Docker Compose. No local Java installation required!

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed.

## How to Run

### Option 1: Using Docker Compose (Recommended)

This is the easiest way. It builds the project and starts the server automatically.

1.  Open your terminal/command prompt in the project folder.
2.  Run:
    ```bash
    docker-compose up --build
    ```
3.  Wait for the logs to say `Started ChatApplication`.
4.  Open [http://localhost:8080/index.html](http://localhost:8080/index.html) in your browser.

To stop the server, press `Ctrl+C` in the terminal.

### Option 2: Using standard Docker

1.  Build the image:
    ```bash
    docker build -t java-chat-server .
    ```
2.  Run the container:
    ```bash
    docker run -p 8080:8080 java-chat-server
    ```

## Project Structure

- `src/main/java`: Backend source code (Spring Boot).
  - `config/WebSocketConfig.java`: Configures STOMP endpoint `/ws`.
  - `controller/ChatController.java`: Handles message routing.
  - `model/ChatMessage.java`: Message data structure.
- `src/main/resources/static/index.html`: The frontend client.
- `Dockerfile`: Multi-stage build configuration.
- `docker-compose.yml`: Orchestration file.

## Troubleshooting

- **Port 8080 already in use**: Stop any other services running on port 8080 or change the mapping in `docker-compose.yml` (e.g., `"8081:8080"`).
- **Build fails**: Ensure you have a stable internet connection for downloading Maven dependencies on the first run.
