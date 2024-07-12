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

## Improvements and Production Considerations

### 1. Production Database Configuration

For production environments, consider the following database configuration improvements:

- Use a robust, production-grade database such as PostgreSQL or MySQL instead of an in-memory database.
- Configure database connection pooling for improved performance and resource management.
- Implement database migration tools like Flyway or Liquibase for version control of database schemas.

Example production database configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://your-db-host:5432/wallet_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Connection pooling
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
```

### 2. Security Enhancements

- Implement authentication and authorization using Spring Security.
- Use HTTPS for all API endpoints in production.
- Implement rate limiting to prevent abuse of the API.
- Add input validation and sanitization to prevent injection attacks.

### 3. Monitoring and Observability

- Integrate with application performance monitoring (APM) tools like New Relic or Datadog.
- Implement health check endpoints for easier monitoring and deployment.
- Use distributed tracing for better visibility into microservices interactions.

### 4. Caching

- Implement caching mechanisms for frequently accessed data using tools like Redis or Ehcache.

### 5. API Documentation

- Add Swagger/OpenAPI documentation for easier API consumption and testing.

### 6. Containerization

- Dockerize the application for easier deployment and scaling.
- Create Kubernetes deployment configurations for orchestration in production environments.

### 7. Continuous Integration and Deployment (CI/CD)

- Set up CI/CD pipelines using tools like Jenkins, GitLab CI, or GitHub Actions.
- Implement automated testing, including unit tests, integration tests, and end-to-end tests.

### 8. Error Handling and Resilience

- Implement a global exception handler for consistent error responses.
- Use circuit breakers (e.g., Resilience4j) for improved fault tolerance.

### 9. Scalability

- Consider implementing event-driven architecture for better scalability.
- Use message queues (e.g., RabbitMQ, Apache Kafka) for asynchronous processing of transactions.

### 10. Auditing and Compliance

- Implement auditing mechanisms to track changes to sensitive data.
- Ensure compliance with relevant financial regulations and data protection laws.

By implementing these improvements, the Wallet API will be better suited for production environments, offering increased security, scalability, and maintainability.

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