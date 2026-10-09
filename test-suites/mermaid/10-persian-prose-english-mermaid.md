# مستند فنی احراز هویت و امنیت شبکه (متن فارسی با دیاگرام انگلیسی)

این سند برای بررسی عملکرد حالت دو زبانه پلاگین طراحی شده است: **متن توضیحات کاملاً فارسی و راست‌چین (RTL)** است، اما **دیاگرام‌های Mermaid تماماً انگلیسی و چپ‌چین (LTR)** هستند.

## هدف تست
* پاراگراف‌ها، عنوان‌ها و لیست‌های فارسی باید با فونت `وزیرمتن` (Vazirmatn) و با چینش دقیق راست به چپ رندر شوند.
* دیاگرام‌های Mermaid چون متون داخلشان انگلیسی خالص است، باید بدون اعمال جهت RTL یا فونت فارسی، با فونت `JetBrains Mono` و چینش استاندارد چپ‌به‌راست نمایش داده شوند.
* بلوک‌های کد (`pre` و `code`) و دیاگرام‌ها نباید ساختار راست‌به‌چپ متن را به هم بریزند (Code & Diagram Isolation).

---

## ۱. معماری احراز هویت با پروتکل OAuth 2.0 و JWT

در این سناریو، کاربر ابتدا از طریق درگاه احراز هویت متمرکز لاگین کرده و توکن‌های دسترسی دریافت می‌کند:

```mermaid
sequenceDiagram
    autonumber
    actor Client as Web Browser
    participant Gateway as Kong API Gateway
    participant Auth as Identity Provider
    participant Service as Protected Microservice
    participant Redis as Redis Session Cache

    Client->>Gateway: POST /auth/login (email, password)
    Gateway->>Auth: Validate credentials
    Auth->>Redis: Check brute-force rate limit
    Redis-->>Auth: Rate limit OK (allowed)
    Auth-->>Gateway: Generate Access Token + Refresh Token
    Gateway-->>Client: 200 OK (Set HttpOnly Cookies)

    Note over Client,Gateway: Subsequent API requests with Bearer Token
    Client->>Gateway: GET /api/v2/orders
    Gateway->>Gateway: Verify JWT RS256 signature
    Gateway->>Service: Forward request (X-User-Id: 98124)
    Service-->>Gateway: 200 OK (Orders JSON payload)
    Gateway-->>Client: 200 OK Response
```

---

## ۲. دیاگرام جریان مسیریابی درخواست‌ها در زیرساخت ابری

توضیحات جریان ترافیک:
1. ابتدا درخواست‌های کاربران به شبکه توزیع محتوا (CDN) وارد می‌شوند.
2. ترافیک پس از بررسی فایروال به لود بالانسر و سپس سرویس‌های کانتینری هدایت می‌شود.

```mermaid
graph TD
    User([End Users]) --> Edge[Cloudflare Global Anycast]
    Edge --> Firewall[WAF & DDoS Mitigation]
    Firewall --> Ingress[Ingress Controller NGINX]

    subgraph InternalServices["Backend Microservices Cluster"]
        Ingress --> APIGateway[API Gateway Router]
        APIGateway --> UserService[User Management Service]
        APIGateway --> BillingService[Billing & Invoicing Service]
        APIGateway --> NotificationService[Push Notification Service]

        UserService --> UserDB[(PostgreSQL Primary)]
        BillingService --> PaymentMQ[RabbitMQ Message Broker]
        PaymentMQ --> WorkerPool[Asynchronous Payment Workers]
    end
```

---

## ۳. جدول وضعیت‌های صف پیام (State Diagram)

وضعیت رویدادها در صف پیام‌رسان هنگام پردازش غیرهمگام:

```mermaid
stateDiagram-v2
    [*] --> Enqueued : Task published
    Enqueued --> InProgress : Worker picked up task
    
    state InProgress {
        [*] --> ValidatingPayload
        ValidatingPayload --> ExecutingJob : Payload valid
        ExecutingJob --> AwaitingThirdParty : External HTTP Call
        AwaitingThirdParty --> Succeeded : Response 200 OK
    }

    InProgress --> Completed : Acknowledge ACK
    InProgress --> DeadLetterQueue : Max retries exceeded (3 times)
    InProgress --> Retrying : Transient network error (Backoff)
    Retrying --> Enqueued : Re-queue message
    Completed --> [*]
    DeadLetterQueue --> [*]
```

---

## نتیجه‌گیری و بررسی صحت نمایش
* بررسی کنید که متون فارسی این صفحه کاملاً راست‌چین و بدون درهم‌ریختگی باشند.
* بررسی کنید که لیبل‌های تمام باکس‌ها، عنوان‌های گره‌ها و متن پیام‌های سکوئنس در دیاگرام با فونت انگلیسی مونو‌اسپیس و جهت چپ‌به‌راست نمایش یابند.
