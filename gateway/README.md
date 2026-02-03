# UniHub API Gateway

## Overview

The **UniHub API Gateway** is the **single entry point (edge server)** for all client requests in the UniHub ecosystem.  
It provides a unified, meaningful REST interface for clients while routing requests internally to the appropriate microservices.

The gateway is responsible for **routing, security, JWT validation, path rewriting, and cross-cutting concerns** that should not be duplicated across microservices.

---

## Core Responsibilities

- Act as a **single edge server** for all UniHub clients
- Rewrite external paths into internal microservice routes
- Perform **JWT validation** for incoming requests
- Enforce **role-based access control**
- Protect internal and sensitive endpoints
- Forward authenticated user context to downstream services
- Apply **CORS configuration**
- Validate token revocation using Redis (blacklisted tokens)

---

## Architecture Role

- Clients communicate **only with the Gateway**
- Internal microservices are **not directly exposed**
- Service discovery is used to route requests dynamically
- Centralized security enforcement at the gateway level

---

## Path Rewriting & Routing

The gateway rewrites external, client-friendly paths into internal microservice paths using **Spring Cloud Gateway**.

### Example Routing Rules

| External Path Prefix | Target Microservice | Internal Path |
|---------------------|--------------------|---------------|
| `/unihub/subscription/**` | SUBSCRIPTION | `/**` |
| `/unihub/universitymanagement/**` | UNIVERSITYMANAGEMENT | `/**` |
| `/unihub/s3/**` | S3 | `/**` |

This allows clients to use a **consistent API namespace** while keeping internal service URLs hidden.

---

## Security Model

### JWT Validation
- Every protected request must include a valid **Bearer JWT**
- Tokens are validated at the gateway before routing
- JWT signature and claims are verified
- Invalid or expired tokens are rejected immediately

### Redis Token Blacklisting
- Redis is used to check whether a JWT is blacklisted
- Blacklisted tokens (e.g., after logout) are rejected
- Key format example:

---

## Role-Based Access Control

Authorization rules are enforced at the gateway level.

### Examples

| Path | Access Rule |
|----|------------|
| `/unihub/subscription/api/v1/public/**` | Public |
| `/unihub/subscription/api/v1/customer-service/**` | `ROLE_CUSTOMER_SERVICE` |
| `/unihub/subscription/api/v1/system-admin/**` | `ROLE_SYSTEM_ADMIN` |
| `/unihub/universitymanagement/api/v1/internal/**` | ❌ Denied |
| `/unihub/s3/**` | Authenticated users only |

This ensures:
- Internal endpoints are never exposed
- Business services remain lightweight and focused

---

## Forwarding Authenticated User Context

After successful JWT validation, the gateway **extracts user data from the token** and forwards it to downstream services via HTTP headers.

### Forwarded Headers

| Header | Description |
|------|------------|
| `X-User-Email` | Authenticated user email |
| `X-User-University-Id` | University identifier |

### Purpose
- Audit logging
- Business logic decisions
- Ownership & authorization checks
- Avoid repeated JWT parsing in microservices

---

## CORS Configuration

The gateway centrally manages **CORS policies**.

### Current Configuration
- Allowed Origins: `http://localhost:3000`
- Allowed Methods: `GET, POST, PUT, DELETE, OPTIONS, PATCH`
- Allowed Headers: `*`
- Credentials: Allowed
- Max Age: `3600s`

This ensures frontend clients can safely interact with the backend.

---

## Technology Stack

- Java 21+
- Spring Boot
- Spring Cloud Gateway
- Spring WebFlux
- Spring Security (Reactive)
- JWT
- Redis
- Service Discovery (Eureka / Consul)
- Lombok

---

## Key Components

### JWT Validator Filter
- Extracts JWT from Authorization header
- Validates signature and claims
- Checks Redis for blacklisted tokens
- Builds `Authentication` object
- Injects user context into request headers

### Security Configuration
- Centralized authorization rules
- CORS configuration
- Stateless security model
- CSRF disabled (API-based)

### Route Locator
- Dynamic routing via service discovery
- Clean external API paths
- Internal service abstraction

---

## Why Gateway-Level Security?

- Eliminates duplicated security logic
- Ensures consistent access control
- Protects internal microservice endpoints
- Simplifies downstream services
- Improves observability and auditing

---

## Notes

- The Gateway **must always be deployed before other services**
- Internal services should never expose public ports
- Redis availability is critical for token revocation
- Any new microservice must be registered in the gateway routes

---

## Future Enhancements

- Rate limiting per user / IP
- Circuit breakers and retries
- Distributed tracing (OpenTelemetry)
- API versioning strategy
- Multi-tenant routing rules

---

**UniHub – API Gateway & Edge Security Service**
