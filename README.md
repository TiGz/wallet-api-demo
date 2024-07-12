# Wallet API

## Overview

The Wallet API is a Spring Boot application that provides a RESTful interface for managing digital wallets. It allows users to perform various operations such as adding funds, withdrawing funds, viewing transactions, and retrieving wallet information.

## Features

- Add funds to a wallet
- Withdraw funds from a wallet
- Retrieve wallet information
- View paginated transaction history
- Retryable wallet operations for improved reliability

## Technologies Used

- Java 8+
- Spring Boot
- Spring Data JPA
- Maven
- SLF4J for logging

## Project Structure

The project follows a standard Spring Boot application structure:

```
src
├── main
│   ├── java
│   │   └── org.github.tigz.wallet
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── model
│   │       ├── repository
│   │       └── service
│   └── resources
│       └── application.properties
└── test
    └── java
        └── org.github.tigz.wallet
            └── service
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven

### Building the Project

To build the project, run the following command in the project root directory:

```
./mvnw clean install
```

### Running the Application

To run the application, use the following command:

```
./mvnw spring-boot:run
```

The application will start and be available at `http://localhost:8080` (or the port specified in your `application.properties`).

## API Endpoints

| Method | Endpoint                          | Description                                |
|--------|-----------------------------------|--------------------------------------------|
| POST   | `/api/wallet/{customerId}/add`    | Add funds to a customer's wallet           |
| POST   | `/api/wallet/{customerId}/withdraw`| Withdraw funds from a customer's wallet    |
| GET    | `/api/wallet/{customerId}/transactions` | Get paginated transactions for a wallet |
| GET    | `/api/wallet/{customerId}`        | Get wallet information for a customer      |

## Configuration

The application can be configured using the `application.properties` file located in the `src/main/resources` directory.

## Testing

To run the tests, execute the following command:

```
./mvnw test
```

## Logging

The application uses SLF4J for logging. Log levels and other logging configurations can be adjusted in the `application.properties` file.

## Contributing

Contributions to the Wallet API project are welcome. Please follow these steps to contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Contact

[Adam Chesney] - [ajchesney@gmail.com]

Project Link: [https://github.com/TiGz/wallet-api-demo](https://github.com/TiGz/wallet-api-demo)