# UniHub University Management Microservice

## Overview

The **University Management Microservice** is responsible for managing **universities and their core metadata** within the **UniHub** platform.

It acts as the **authoritative source of truth** for university entities and exposes both:
- **Public / role-protected endpoints** for querying universities
- **Internal endpoints** used by other microservices (mainly the Subscription Microservice)

---

## Core Responsibilities

- Store and manage university data
- Retrieve university details
- Search and paginate universities
- Provide university metadata for customer service
- Create universities internally after successful subscription
- Enforce subscription plan limits
- Serve as an internal dependency for subscription onboarding

---

## Architecture Role

- Universities are created **only internally**, never directly by clients
- Subscription Microservice orchestrates onboarding
- This service focuses strictly on **university domain logic**
- Internal endpoints are protected and **not exposed publicly**

---

## University Queries (Public & Role-Based)

### University Endpoints
**Base Path:** `/api/v1`

| Method | Endpoint | Access | Description |
|------|--------|-------|------------|
| GET | `/get-university/{id}` | Authenticated | Get university details by ID |
| GET | `/customer-service/get-universities` | Customer Service | List universities (paginated) |
| GET | `/customer-service/search-university` | Customer Service | Search universities by name |

---

### Endpoint Details

#### Get University by ID
Returns full university information.


#### List Universities (Paginated)
Supports pagination and sorting.


**Query Parameters**
- `page_num` (default: 1)
- `sort_dir` (default: desc)
- `sort_field` (default: createdAt)

#### Search Universities
Allows searching universities by text.

