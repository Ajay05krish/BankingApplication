# Banking Application 🚀

A microservice-based **banking application** developed using **Spring Boot** and **MySQL**. The application includes **user management**, **account management**, and **transaction processing** features. It leverages modern Java features from **Java 17** and **Java 21** to ensure efficiency, scalability, and maintainability.

The application also integrates **Logstash**, **Kibana**, and **Elasticsearch** for centralized logging and monitoring, along with an **API Gateway** and **Circuit Breaker** for fault tolerance and resilience. **WebClient** and **CompletableFuture** are used for asynchronous processing.

## Key Features

- **User Management**: Register and manage user profiles.
- **Account Management**: Create and manage user accounts, with related operations.
- **Transaction Processing**: Supports deposit, withdrawal, and transfer operations.
  
## Modern Java Features

- **Sealed Classes**: Models different transaction types with fixed, immutable class hierarchies.
- **Pattern Matching**: Simplifies logic, especially for transaction processing.
- **Record Patterns**: Uses immutable records to handle account and transaction data.
- **Sequenced Collections**: Manages transaction order efficiently with Java 21 sequenced collections.
- **Virtual Threads**: Leverages lightweight virtual threads for concurrent transaction processing, improving scalability.

## Architecture & Technologies

- **Microservice-Based**: Independent, scalable services for user, account, and transaction management.
- **API Gateway**: Manages communication between microservices.
- **Circuit Breaker**: Ensures resilience by preventing calls to failed services.
- **Elasticsearch, Logstash, and Kibana (ELK Stack)**: Used for centralized logging and monitoring, providing real-time insights.
- **Database**: **MySQL** for persistent storage of user and transaction data.
- **WebClient & CompletableFuture**: Used for handling asynchronous requests, improving performance and scalability.

## Tech Stack

- **Backend**: Spring Boot (Java 17 & 21)
- **Database**: MySQL
- **Logging & Monitoring**: Logstash, Kibana, and Elasticsearch (ELK Stack)
- **API Gateway**: For routing and load balancing between services
- **Resilience**: Circuit Breaker
- **Async Processing**: WebClient, CompletableFuture
- **API**: RESTful APIs

## Getting Started

### Prerequisites

- **Java 17+** and **Spring Boot**
- **MySQL** (or compatible database)
- **Elasticsearch, Logstash, Kibana (ELK Stack)** for logging and monitoring
- **API Gateway** and **Circuit Breaker** libraries

