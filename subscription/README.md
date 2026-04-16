# UniHub Subscription Microservice

## Overview

The **Subscription Microservice** is responsible for managing **university subscriptions** within the **UniHub** platform.  
It handles subscription plans, university subscription requests, Stripe-based payments, customer inquiries, and coordinates with other microservices to complete the onboarding flow.

This service acts as the **business orchestrator** for subscriptions and payments.

---

## Core Responsibilities

- Display available subscription plans
- Allow universities to request a subscription
- Handle customer service inquiries related to subscriptions
- Integrate with **Stripe** for payment processing
- Create Stripe checkout sessions
- Assign subscription plans to universities
- Upload and delete files (accreditation, logos) via Kafka
- Create universities by calling the **University Management Microservice**
- Publish subscription-related events using Kafka

---

## High-Level Flow

1. University submits a subscription request with required documents
2. Files are uploaded asynchronously via Kafka → S3 Microservice
3. Customer service reviews the request
4. Stripe checkout session is created
5. Payment is completed
6. Subscription plan is assigned to the university
7. University Management Microservice is invoked to create the university
8. University system admin is provisioned downstream

---

## Subscription Requests

Universities can submit subscription requests along with accreditation and branding files.

### Subscription Request Endpoints

**Base Path:** `/api/v1`

| Method | Endpoint | Access | Description |
|------|--------|-------|------------|
| POST | `/public/request-subscription` | Public | Submit a new subscription request with files |
| GET | `/customer-service/get-request/{id}` | Customer Service | Get subscription request by ID |
| GET | `/customer-service/get-requests` | Customer Service | List subscription requests with filtering |
| PATCH | `/customer-service/update-request-status/{id}` | Customer Service | Update subscription request status |
| DELETE | `/customer-service/delete-request/{id}` | Customer Service | Delete a subscription request |

---

## Subscription Plans

The microservice manages subscription plans that can later be assigned to universities.

### Subscription Plan Endpoints

**Base Path:** `/api/v1/subscription-plans`

| Method | Endpoint | Access | Description |
|------|--------|-------|------------|
| POST | `/customer-service/create` | Customer Service | Create a new subscription plan |
| GET | `/customer-service/{id}` | Customer Service | Get subscription plan details |
| GET | `/all` | Public | List all available subscription plans |
| PUT | `/customer-service/update/{id}` | Customer Service | Update subscription plan |
| DELETE | `/customer-service/delete/{id}` | Customer Service | Delete subscription plan |
| POST | `/system-admin/set-university-subscription/{id}` | System Admin | Assign a plan to a university |

---

## Stripe Integration

This microservice integrates directly with **Stripe** to handle subscription payments.

### Stripe Responsibilities

- Create Stripe checkout sessions
- Handle payment redirection
- Validate successful and canceled payments
- Link payments to subscription plans

### Stripe Endpoint

**Base Path:** `/api/v1/system-admin/stripe`

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/create-session/{id}` | Create Stripe checkout session |

---

## File Handling (Kafka + S3)

Files (accreditation documents, logos) are **not uploaded directly to S3**.

Instead:
- Files are published as Kafka events
- The **S3 Microservice** consumes these events
- Upload and deletion are fully asynchronous

### Kafka Topics Used

| Topic | Purpose |
|-----|--------|
| `upload-file-topic` | Upload files to S3 |
| `delete-file-topic` | Delete files from S3 |
| `set-plan-topic` | Assign subscription plan to a university |

---

## Inter-Service Communication

### University Management Microservice

The Subscription Microservice uses **OpenFeign** to communicate with the University Management Microservice.

#### Responsibilities
- Create a university after subscription approval
- Check subscription plan limits

#### Internal Endpoints Used

| Method | Endpoint | Purpose |
|------|--------|--------|
| POST | `/api/v1/internal/create` | Create university |
| GET | `/api/v1/internal/get-subscription-plan-count/{id}` | Validate plan limits |

### Resilience
- Feign Circuit Breaker enabled
- Fallbacks provided using Resilience4j

---

## Configuration Management

All configuration is fetched from the **Config Server**.

### Kafka Configuration

```yaml
spring:
  cloud:
    stream:
      bindings:
        uploadFile-out-0:
          destination: upload-file-topic
        deleteFile-out-0:
          destination: delete-file-topic
        setUniversitySubscriptionPlan-out-0:
          destination: set-plan-topic
