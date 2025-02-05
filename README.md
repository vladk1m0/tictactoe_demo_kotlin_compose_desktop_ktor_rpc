# TicTacToe Demo

## Purpose

The purpose of this project is to demonstrate a Kotlin-based desktop application using Jetpack Compose for the user interface. It includes a server component to manage game logic and a client component to interact with the server, specifically for a Tic-Tac-Toe game.

## Functionality
- Multiplayer network gameplay (supports a 3x3 grid for Tic-Tac-Toe games).
- Server and client components for managing game logic and user interactions.

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/vladkimo/tictactoe_demo_kotlin_compose_desktop_ktor_rpc.git
   cd tictactoe_demo_kotlin_compose_desktop_ktor_rpc
   ```

## Setup

### Prerequisites

- **Operating System**: macOS, Windows, or Linux
- **JDK**: Version 11 or higher
- **Gradle**: Version 7.0 or higher

## Usage

### Building and Running

To build and run the application, use the Gradle tool window by clicking the Gradle icon in the right-hand toolbar, or run it directly from the terminal:

```sh
./gradlew run  # Build and run the application
./gradlew build  # Only build the application
./gradlew check  # Run all checks, including tests
./gradlew clean  # Clean all build outputs
```

### Running Server and Client

#### Server
1. Open the terminal and navigate to the project root directory.
2. Run the following command to build and run the server:
   ```sh
   ./gradlew :server:run
   ```

#### Client
1. Open the terminal and navigate to the project root directory.
2. Run the following command to build and run the client:
   ```sh
   ./gradlew :client:run
   ```

## Dependencies

### Server

- **Ktor**: Framework for building asynchronous servers and clients in connected systems.
- **Kotlinx RPC**: Library for remote procedure calls.
- **Kotlinx Serialization**: Library for serializing Kotlin data classes.

### Client

- **Kotlin Standard Library**: Core library for Kotlin programming.
- **Compose Desktop**: Modern toolkit for building desktop UI.
- **Kotlin Coroutines**: Library for asynchronous programming.
- **StateFlow**: State management library for Kotlin.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
