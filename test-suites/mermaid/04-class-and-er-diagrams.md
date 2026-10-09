# نمودارهای کلاس، موجودیت و وضعیت (Class, ER & State Diagrams)

این سند برای بررسی پشتیبانی کامل افزونه از انواع نمودارهای پرکاربرد مهندسی نرم‌افزار در مِرمید (شامل `classDiagram`، `erDiagram` و `stateDiagram-v2`) طراحی شده است.

## هدف آزمون
- اطمینان از عملکرد موتور `mermaid.min.js` داخلی در رسم دیاگرام‌های پیشرفته غیر از Flowchart
- بررسی صحت رندر سمبل‌های رابطه‌ای (`||--o{`، `--|>`، `*--` و ...)
- نمایش صحیح تایپ‌ها و متدها در ساختارهای جدولی دیاگرام

---

## سناریوی ۱: نمودار کلاس شی‌گرا (Class Diagram)

```mermaid
classDiagram
    direction TB
    class BaseEntity {
        <<abstract>>
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +isNew() Boolean
    }

    class UserAccount {
        +String username
        +String email
        +String passwordHash
        +UserStatus status
        +verifyPassword(String raw) Boolean
        +changeEmail(String newEmail) void
    }

    class Order {
        +String trackingCode
        +BigDecimal totalAmount
        +OrderStatus status
        +addItem(OrderItem item) void
        +calculateFinalPrice() BigDecimal
    }

    class OrderItem {
        +Long productId
        +String productName
        +Integer quantity
        +BigDecimal unitPrice
        +getSubtotal() BigDecimal
    }

    BaseEntity <|-- UserAccount : ارث‌بری
    BaseEntity <|-- Order : ارث‌بری
    BaseEntity <|-- OrderItem : ارث‌بری
    UserAccount "1" --> "*" Order : ثبت سفارش
    Order "1" *-- "many" OrderItem : ترکیب (Composition)
```

---

## سناریوی ۲: نمودار رابطه موجودیت‌ها (ER Diagram)

```mermaid
erDiagram
    CUSTOMER ||--o{ ORDER : places
    ORDER ||--|{ LINE-ITEM : contains
    CUSTOMER }|..|{ DELIVERY-ADDRESS : uses
    PRODUCT ||--o{ LINE-ITEM : ordered-in

    CUSTOMER {
        bigint id PK
        varchar(100) full_name
        varchar(100) phone_number UK
        varchar(255) email
        timestamp registered_at
    }

    ORDER {
        bigint id PK
        bigint customer_id FK
        varchar(32) order_number UK
        varchar(20) status
        decimal grand_total
        timestamp created_at
    }

    LINE-ITEM {
        bigint id PK
        bigint order_id FK
        bigint product_id FK
        int quantity
        decimal unit_price
    }

    PRODUCT {
        bigint id PK
        varchar(150) title
        varchar(64) sku UK
        decimal regular_price
        int stock_count
    }
```

---

## سناریوی ۳: ماشین وضعیت چرخه عمر سفارش (State Diagram)

```mermaid
stateDiagram-v2
    [*] --> در_انتظار_پرداخت : ثبت سفارش جدید
    در_انتظار_پرداخت --> پرداخت_شده : موفقیت تراکنش بانکی
    در_انتظار_پرداخت --> لغو_شده : انقضای مهلت (Timeout) یا انصراف کاربر

    state پرداخت_شده {
        [*] --> تخصیص_انبار
        تخصیص_انبار --> بسته‌بندی_کالا : موجودی رزرو شد
        بسته‌بندی_کالا --> آماده_ارسال : بسته‌بندی تکمیل شد
    }

    پرداخت_شده --> ارسال_شده : تحویل به مامور پست / پیک
    ارسال_شده --> تحویل_شده : تایید دریافت توسط مشتری
    ارسال_شده --> مرجوع_شده : عدم حضور مشتری یا مغایرت کالا
    
    تحویل_شده --> [*]
    لغو_شده --> [*]
    مرجوع_شده --> بازگشت_وجه : بررسی در انبار بازگشتی
    بازگشت_وجه --> [*]
```

---

## موارد ارزیابی در پیش‌نمایش
1. رندر شدن صحیح همه ۳ نوع نمودار بدون پرتاب خطای جاوااسکریپت.
2. تفکیک فونت انگلیسی در متدها و فیلدهای انگلیسی و استفاده از فونت فارسی برای برچسب‌های فارسی وضعیت‌ها.
3. عملکرد روان بزرگ‌نمایی و جابه‌جایی (Pan & Zoom) در هر ۳ کارت نمودار.
