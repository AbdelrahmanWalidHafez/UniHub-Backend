# UniHub Spring Cloud Config Server

## What is Spring Cloud Config Server?

**Spring Cloud Config Server** is a centralized configuration server provided by **Spring Cloud**.

It allows microservices to:
- Fetch configuration from a **Git repository** (or local filesystem, Vault, etc.)
- Centralize configuration management for multiple environments
- Dynamically refresh configuration if integrated with **Spring Cloud Bus**

This ensures consistency and reduces configuration duplication across services.

---

## Config Server Purpose in UniHub

In **UniHub**, Config Server is responsible for:

- Providing centralized configuration to all microservices (Auth, Chat, Payments, etc.)
- Allowing dynamic refresh of configuration when using Spring Cloud Bus
- Supporting multiple environments (dev, test, prod) from a single Git repository
- Decoupling configuration from individual microservice code

All backend services fetch their configuration from the Config Server at startup.

---

## Relationship with other Microservices

- **Config Server**
    - Manages centralized configuration
    - Runs independently

- **Eureka Server**
    - Handles service discovery only
    - Microservices register themselves here

- **Microservices**
    - Fetch configuration from Config Server
    - Register themselves in Eureka Server

This separation avoids circular dependencies and improves system stability.

---

## Getting Started

### Prerequisites
- Java 21+
- Maven
- IntelliJ IDEA (recommended)
- Git access to the configuration repository
- (Optional) RabbitMQ if using Spring Cloud Bus

### Setup Env File
#### Linux / MacOS
```bash
    cp env .env
```
#### Windows
```bash
    copy env .env
