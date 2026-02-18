# UniHub Auth Microservice

## Overview

The **Auth Microservice** is a core component of the **UniHub** platform.  
It is responsible for **authentication, authorization, token management, and user identity operations** across the system.

This microservice is designed to work within a **Spring Cloud Microservices Architecture** and integrates with centralized configuration, relational and in-memory databases, and other internal UniHub services.

---

## Key Responsibilities

- User authentication (Login)
- Token lifecycle management (Access & Refresh Tokens)
- Token revocation (Logout)
- Fetching authenticated user information
- Internal system user creation
- Bootstrapping system roles and default users
- Secure communication with other microservices

---

## Configuration Management

- Fetches all configuration properties from the **Config Server**
- No environment-specific configuration is hardcoded
- Supports centralized configuration management

---

## Databases

### PostgreSQL
Used for persistent storage of:
- Users
- Roles
- Authorities
- Authentication-related entities

### Redis
Used for:
- Storing **refresh tokens**
- Storing **blacklisted access tokens** after logout
- Fast token validation and revocation

---

## Security Model

- JWT-based authentication
- Short-lived **Access Tokens**
- Long-lived **Refresh Tokens**
- Logout operation revokes and blacklists tokens in Redis
- Role-based access control (RBAC)

---

## Data Loader (Bootstrap Initialization)

On application startup, a data loader initializes essential system data.

### Roles Created Automatically
- `ROLE_SYSTEM_ADMIN`
- `ROLE_CUSTOMER_SERVICE`

### Default Customer Service User

| Field | Value |
|-----|------|
| Email | `customer_service@example.com` |
| Password | `password` |
| Role | `ROLE_CUSTOMER_SERVICE` |

> ⚠️ **Important:**  
> These credentials are intended for development/testing purposes only and **must be changed or disabled in production environments**.

---

## University System Admin Creation Flow

1. A university completes the **subscription process**
2. The **University Microservice** validates and accepts the subscription
3. The University Microservice invokes an **internal Auth endpoint**
4. The Auth Microservice:
   - Creates a **System Admin user**
   - Assigns the role `ROLE_SYSTEM_ADMIN`
5. The created user can:
   - Log in to the system
   - Subscribe and manage university-related features

---

## REST API Endpoints

### Authentication Endpoints  
**Base Path:** `/api/v1/auth`

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/login` | Authenticate user and generate access & refresh tokens |
| POST | `/refresh` | Refresh access token using a valid refresh token |
| POST | `/logout` | Revoke access and refresh tokens |
| GET | `/user-info` | Retrieve authenticated user information |

---

### Internal Endpoints  
**Base Path:** `/api/v1/internal/user`

> ⚠️ **Internal Use Only**  
> These endpoints are intended to be called **only by other UniHub microservices**.

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/create` | Create a system admin user for a university |

---

## Internal Controller Example

```java
@PostMapping("/create")
public ResponseEntity<SystemAdminResponse> createSystemAdmin(
        @Valid @RequestBody SystemAdminRequest request) {
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(internalService.createSystemAdmin(request));
}
