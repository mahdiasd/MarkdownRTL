# English Sequence Diagram (Pure English Font Test)

This document tests the **English font detection** and **Sequence Diagram** rendering in the **Markdown RTL** plugin.

## Test Objective
As per user requirements:
> When the diagram content is in English, the default code font (`JetBrains Mono`, system monospace) must be applied automatically instead of the Persian font (`Vazirmatn`).

---

## Scenario 1: OAuth2 Authentication & Authorization Code Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as Client Browser
    participant Gateway as API Gateway
    participant Auth as Auth Server (Keycloak)
    participant Resource as Resource Server
    participant DB as PostgreSQL DB

    User->>Gateway: GET /api/v1/profile
    Gateway->>User: 302 Redirect to /login
    User->>Auth: POST /oauth/token (Credentials)
    Auth->>DB: Query User & Credentials
    DB-->>Auth: User Record Found & Validated
    Auth-->>User: Issue JWT Access Token + Refresh Token
    
    User->>Gateway: GET /api/v1/profile (Bearer Token)
    Gateway->>Gateway: Verify JWT Signature & Claims
    Gateway->>Resource: Forward Request with Claims Header
    Resource->>DB: Fetch Profile Details
    DB-->>Resource: Profile Entity
    Resource-->>Gateway: 200 OK (User Profile JSON)
    Gateway-->>User: 200 OK Response
```

---

## Scenario 2: Async Event-Driven Message Processing

```mermaid
sequenceDiagram
    participant Publisher as Order Service
    participant Broker as Apache Kafka
    participant Worker as Notification Worker
    participant FCM as Firebase Cloud Messaging
    actor Mobile as Mobile Device

    Note over Publisher,Broker: High-throughput async message pipeline
    Publisher->>Broker: Publish Event: OrderCreated(id=1024)
    Broker-->>Publisher: ACK (Partition=2, Offset=5821)
    
    loop Polling Batch
        Broker->>Worker: Consume message batch
    end
    
    Worker->>Worker: Hydrate customer metadata
    Worker->>FCM: POST /send (Push Notification Payload)
    FCM-->>Mobile: Deliver Push Notification
    Mobile-->>FCM: Delivery Receipt
    FCM-->>Worker: HTTP 200 (Success)
```

---

## Verification Points
1. **Typography**: Does the diagram use `JetBrains Mono` or default monospace font? (Confirm Persian font `Vazirmatn` is **not** applied to this pure English diagram).
2. **Alignment & Orientation**: Is the text aligned LTR with proper participant box paddings?
3. **Interactive Features**: Can you zoom, pan, and open this sequence diagram in the fullscreen modal without layout distortions?
