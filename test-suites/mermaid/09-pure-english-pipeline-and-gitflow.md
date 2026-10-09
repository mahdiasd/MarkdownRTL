# CI/CD Pipeline & Gitflow Release Workflow

This document tests complex multi-step technical diagrams including **Git Graph** and **Class & ER Diagrams** entirely in **English**.

## Pipeline Overview
Continuous Integration and Deployment (CI/CD) pipelines involve automated linting, test runners, container builds, and deployment stages.

---

## 1. Gitflow Branching Strategy

Testing Mermaid `gitGraph` rendering with English commit tags and branch names:

```mermaid
gitGraph
    commit id: "v1.0.0"
    branch develop
    checkout develop
    commit id: "feat: auth service"
    commit id: "feat: redis cache"
    branch feature/payment-gateway
    checkout feature/payment-gateway
    commit id: "add stripe client"
    commit id: "unit tests passing"
    checkout develop
    merge feature/payment-gateway id: "PR #42 merged"
    branch release/v1.1.0
    checkout release/v1.1.0
    commit id: "bump version 1.1.0"
    checkout main
    merge release/v1.1.0 id: "tag: v1.1.0"
    checkout develop
    merge release/v1.1.0 id: "sync develop"
```

---

## 2. CI/CD Deployment Flowchart

```mermaid
flowchart LR
    Dev([Developer Push]) --> Lint[Lint & Typecheck]
    Lint --> Test{Unit & Integration Tests}
    
    Test -- Fail --> Notify[Slack / Email Alert]
    Test -- Pass --> Build[Docker Multi-Stage Build]
    
    Build --> Scan[Trivy Vulnerability Scan]
    Scan --> PushRegistry[Push to AWS ECR]
    
    PushRegistry --> ArgoCD[ArgoCD GitOps Sync]
    
    subgraph K8s["Kubernetes Production"]
        ArgoCD --> Staging[Deploy Staging]
        Staging --> SmokeTest{Smoke Tests}
        SmokeTest -- Pass --> Prod[Canary Rollout 10% -> 100%]
        SmokeTest -- Fail --> Rollback[Automatic Rollback]
    end
```

---

## 3. Entity-Relationship Data Model

```mermaid
erDiagram
    ORGANIZATION ||--o{ TEAM : contains
    TEAM ||--o{ USER : includes
    USER ||--o{ API_KEY : owns
    USER ||--o{ AUDIT_LOG : generates
    
    ORGANIZATION {
        uuid id PK
        string name
        string plan_tier
        timestamp created_at
    }
    
    USER {
        uuid id PK
        uuid org_id FK
        string email
        string password_hash
        boolean is_active
        timestamp last_login
    }
    
    API_KEY {
        string key_hash PK
        uuid user_id FK
        string prefix
        timestamp expires_at
    }
```

---

## Observations
- Pure English prose does not trigger any RTL classes.
- Monospace font `JetBrains Mono` is rendered for all database attributes, branch names, and pipeline stages.
