# UniHub S3 Microservice

## Overview

The **S3 Microservice** is a dedicated service in the **UniHub** platform responsible for **managing files stored in Amazon S3** in an isolated, scalable, and high-performance manner.

This microservice abstracts all S3-related operations from the rest of the system and leverages **Spring Cloud Functions** and **Kafka Streams** to enable **asynchronous, event-driven file processing**.

---

## Core Responsibilities

- Manage Amazon S3 bucket operations independently
- Upload files to S3 using asynchronous Kafka-based processing
- Delete files from S3 using Kafka streams
- Serve files for download via REST API
- Improve performance and scalability using event-driven architecture
- Centralize file storage logic away from business microservices

---

## Architecture Highlights

- **Event-driven design** using Kafka
- **Spring Cloud Function** for function-based message consumption
- **Loose coupling** between producers and S3 operations
- **High throughput** file processing
- **Stateless REST API** for file downloads

---

## Amazon S3 Management

The service connects directly to **Amazon S3** using credentials and configuration provided by the **Config Server**.

### Managed Operations
- File upload
- File deletion
- File download

---

## Asynchronous File Processing (Kafka + Functions)

### Why Kafka?
- Faster processing under load
- Decouples file producers from S3 operations
- Improves system resilience
- Enables future stream processing & scaling

---

## Spring Cloud Functions

The microservice exposes **Kafka-backed functions** instead of traditional REST endpoints for upload and delete operations.

### Available Functions

| Function Name | Purpose | Kafka Topic |
|-------------|--------|------------|
| `uploadFile` | Upload file to S3 | `upload-file-topic` |
| `deleteFile` | Delete file from S3 | `delete-file-topic` |

---

## Function Definitions

### Upload File Function

- Accepts a base64-encoded file payload
- Decodes file content
- Uploads file to Amazon S3
 ---
## REST API Endpoints

### File Download Endpoint
**Base Path:** `/api/v1`

| Method | Endpoint | Description |
|------|--------|------------|
| GET | `/get-file/{key}` | Download file from Amazon S3 |

### Endpoint Behavior

- Retrieves the file from the configured S3 bucket
- Returns raw file bytes
- Sets the correct `Content-Type`
- Uses `Content-Disposition: inline` for browser rendering
