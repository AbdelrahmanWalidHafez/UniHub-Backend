# UniHub

## Project Overview

**UniHub** is a comprehensive microservices-based platform designed for managing universities, subscriptions, and file storage in a unified ecosystem. This project is developed as part of a **graduation project** in the College of Computing and Information Technology.  

UniHub provides functionality for:  

- University management and metadata handling  
- Subscription plan management integrated with **Stripe** for payments  
- User authentication and authorization with **JWT** tokens  
- File storage and retrieval using **Amazon S3**  
- API Gateway with routing, security, and rate limiting  
- Event-driven communication via **Kafka**  

The project simulates a real-world scalable system with **modern microservices architecture**, enabling hands-on experience with distributed systems, cloud integration, and enterprise-grade backend solutions.

---

## Architecture

The project is built using **Spring Boot** and is organized into the following microservices:

| Microservice | Responsibilities |
|--------------|-----------------|
| **Auth Service** | Handles login, logout, refresh tokens, user info, role management, and JWT-based authentication. Integrates with PostgreSQL and Redis for user and token storage. |
| **Gateway Service** | Acts as the single entry point, performing path rewriting, JWT validation, rate limiting, CORS configuration, and forwarding authenticated user info to downstream services. |
| **S3 Service** | Manages file uploads and downloads to Amazon S3. Uses Kafka streams for faster processing. |
| **Subscription Service** | Manages subscription plans, requests, payments via Stripe, and customer inquiries. Communicates with university management to create universities and assign subscriptions. |
| **University Management Service** | Manages universities, metadata, and internal endpoints for creating universities and retrieving subscription-related data. |

---

## Key Features

- **Authentication & Authorization**  
  - Login, logout, refresh tokens  
  - Role management (`SYSTEM_ADMIN`, `CUSTOMER_SERVICE`)  
  - JWT tokens with Redis-based blacklist for revoked tokens  

- **API Gateway**  
  - Centralized routing for all microservices  
  - JWT validation and security enforcement  
  - Rate limiting by IP address using Redis  
  - CORS configuration for front-end access  

- **File Storage**  
  - Upload and download files via S3  
  - Kafka streams for faster processing  

- **Subscription Management**  
  - Stripe integration for payments  
  - University subscription request handling  
  - Customer service inquiries management  

- **University Management**  
  - University creation and metadata management  
  - Integration with subscription service for subscription assignment  

---

## Technologies Used

- Java 21 / Spring Boot  
- Spring Cloud (Gateway, Config, Discovery Client)  
- PostgreSQL & Redis  
- Amazon S3  
- Kafka  
- Stripe API  
- JWT for authentication  

---

## NOTICE
a lot of other featchures comming soon!
