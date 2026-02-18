# UniHub Eureka Server

##  What is Eureka Server?

**Eureka Server** is a **Service Discovery Server** provided by **Spring Cloud Netflix**.

It acts as a **central registry** where microservices can:
- Register themselves
- Discover other services dynamically
- Communicate using service names instead of fixed IPs or ports

This removes tight coupling between services and enables scalability and resilience.

---

##  Eureka Server Purpose in UniHub

In **UniHub**, Eureka Server is a **core infrastructure component** responsible for:

-  Registering all UniHub microservices
-  Allowing services to discover each other dynamically
-  Enabling client-side load balancing
-  Supporting fault tolerance and scaling

All backend services (Auth, Chat, Payments, etc.) register with Eureka on startup.

---
##  Relationship with Spring Cloud Config

- **Config Server**
    - Manages centralized configuration
    - Pulls configs from a Git repository

- **Eureka Server**
    - Handles service discovery only
    - Runs independently from Config Server

- **Microservices**
    - Fetch configuration from Config Server
    - Register themselves in Eureka Server

This separation avoids circular dependencies and improves system stability.

---
##  Getting Started
- Java 21+
- Maven
- IntelliJ IDEA (recommended)
- (Optional) RabbitMQ (only if Spring Cloud Bus is used)
- create .env file Linux/Macos
```bash
  cp env .env
```
create .env file Windows
```bash
    copy env .env

