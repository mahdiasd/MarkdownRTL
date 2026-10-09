# Pure English Microservices Architecture & State Machine

This test document is written completely in **English** with standard Left-to-Right (LTR) prose, testing pure English text rendering alongside English **Mermaid** state and architecture diagrams.

## Expected Behavior
1. Document prose should remain strictly **Left-to-Right (LTR)**.
2. English headings, lists, and paragraphs should use English/Latin typography with left alignment.
3. Diagrams contain only ASCII/English labels, so they must automatically adopt **JetBrains Mono** font typography without any Persian/Arabic font substitution.
4. Diagram controls (Zoom, Pan, Source toggle, Copy) should function smoothly.

---

## 1. System Distributed Architecture

The following flowchart outlines the backend infrastructure for a high-availability cloud workload.

```mermaid
graph TD
    Client[Web & Mobile Clients] --> Cloudflare[Cloudflare CDN & WAF]
    Cloudflare --> ALB[AWS Application Load Balancer]
    
    subgraph VPC["Virtual Private Cloud (AWS eu-central-1)"]
        ALB --> Ingress[Kubernetes NGINX Ingress]
        
        subgraph Cluster["EKS Microservices Cluster"]
            Ingress --> AuthSvc[Auth Service :8081]
            Ingress --> OrderSvc[Order Service :8082]
            Ingress --> PaymentSvc[Payment Service :8083]
            
            OrderSvc --> RedisCache[(Redis Cluster)]
            OrderSvc --> KafkaBroker[Apache Kafka Queue]
            PaymentSvc --> StripeAPI[Stripe Gateway API]
        end
        
        KafkaBroker --> AuditWorker[Audit Log Worker]
        AuditWorker --> TimescaleDB[(Timescale Analytics DB)]
        OrderSvc --> PostgresPrimary[(PostgreSQL Primary)]
        PostgresPrimary -. Replication .-> PostgresReplica[(PostgreSQL Read Replica)]
    end
```

---

## 2. Order Lifecycle State Diagram

State machines should preserve strict LTR orientation, arrow paths, and monospace node labels.

```mermaid
stateDiagram-v2
    [*] --> Draft : User creates cart
    Draft --> PendingPayment : Checkout submitted
    
    state PendingPayment {
        [*] --> AwaitingGateway
        AwaitingGateway --> Authorizing : Card details sent
        Authorizing --> Captured : 3D-Secure verified
        Authorizing --> Failed : Insufficient funds
    }
    
    PendingPayment --> Processing : Payment success webhook
    PendingPayment --> Cancelled : Timeout (15 mins)
    
    Processing --> Shipped : Warehouse dispatched
    Shipped --> Delivered : Carrier signature confirmed
    Delivered --> Closed : 14 days refund window passed
    
    Processing --> Refunded : Customer cancellation
    Failed --> Draft : Retry payment
    Cancelled --> [*]
    Closed --> [*]
    Refunded --> [*]
```

---

## Verification Checklist
- [ ] Heading 1 and Heading 2 are left-aligned.
- [ ] Diagram text uses `JetBrains Mono` without Persian fonts.
- [ ] Fullscreen zoom and pan controls operate without layout glitch.
- [ ] Toggle to "Source" view preserves exact indentation.
