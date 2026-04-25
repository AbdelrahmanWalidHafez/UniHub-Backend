# UniHub Mail Microservice

## Overview

The **Mail Microservice** is a core component of the UniHub platform responsible for handling all outgoing email communications. It is designed as an event-driven service that listens to Kafka topics and sends transactional emails such as subscription acceptance and rejection notifications.

This service is fully decoupled from other microservices and communicates asynchronously using **Apache Kafka** and **Spring Cloud Stream**, ensuring scalability, reliability, and fault tolerance.

---

## Responsibilities

The Mail Microservice is responsible for:

* Sending **subscription acceptance emails**
* Sending **subscription rejection emails**
* Rendering **HTML-based email templates**
* Managing **SMTP configuration and delivery**
* Consuming events from Kafka topics

---

## Architecture

* **Architecture Style:** Event-driven Microservice
* **Communication:** Asynchronous messaging via Kafka
* **Framework:** Spring Boot + Spring Cloud Stream
* **Email Provider:** SMTP (configurable)

### Event Flow

1. Another microservice (e.g., Subscription Service) publishes an event to Kafka
2. The Mail Microservice consumes the event
3. The service processes the payload
4. An HTML email is rendered
5. The email is sent via SMTP

---

## Kafka Integration

### Consumed Topics

| Topic Name                    | Description                                        |
| ----------------------------- | -------------------------------------------------- |
| `send-email-acceptance-topic` | Sends acceptance emails for approved subscriptions |
| `send-email-rejection-topic`  | Sends rejection emails for declined subscriptions  |

### Spring Cloud Stream Binding

The service uses the **Spring Cloud Function model**:

* Function name determines the Kafka binding
* Input bindings follow the pattern: `<functionName>-in-0`

Example:

* Function: `sendAcceptanceMail`
* Binding: `sendAcceptanceMail-in-0`
* Destination: `send-email-acceptance-topic`

---

## Email Templates

* Emails are written in **HTML**
* Gmail-safe layout and styling
* Inline images are supported (CID) or public HTTPS assets

### Supported Email Types

* Subscription Accepted
* Subscription Rejected

Each template is designed to be:

* Responsive
* Brand-consistent
* Compatible with major email clients

---

## Configuration

Configuration details (Kafka bindings, SMTP credentials, and environment variables) are intentionally omitted from this document to keep the focus on **architecture, responsibilities, and system role**. Configuration is managed externally via environment variables and centralized configuration services.

---

## Fault Tolerance

* Uses **Resilience4j** (if enabled by upstream services)
* Asynchronous messaging ensures no tight coupling
* Email failures do not block core business flows

---

## Security Considerations

* No inbound HTTP exposure required
* SMTP credentials are secured via environment variables
* No user-sensitive data is persisted
* Kafka communication is isolated per consumer group

---

## Running the Service

### Prerequisites

* Java 21+
* Apache Kafka
* SMTP credentials
* Config Server (optional)

---

## Role in UniHub

The Mail Microservice improves UniHub by:

* Centralizing email logic
* Reducing coupling between services
* Enabling reliable user communication
* Supporting future notification channels

---
