# 📊 Diagramas da Arquitetura

Documentação visual completa da arquitetura, fluxos e componentes do projeto usando Mermaid.

## 🏗️ Clean Architecture - 5 Camadas

```mermaid
graph TB
    subgraph "1. REST API Layer"
        AC["🌐 AuthController<br/>POST /auth/validate"]
        UC["🌐 UserController<br/>CRUD Operations"]
    end
    
    subgraph "2. Business Logic Layer"
        US["💼 UserService<br/>Core Business Rules"]
    end
    
    subgraph "3. Data Access Layer"
        UR["📦 UserRepository<br/>JPA Interface"]
    end
    
    subgraph "4. Domain Model Layer"
        UE["👤 User Entity<br/>Main aggregate"]
        AE["📍 Address<br/>Value Object"]
        RL["👥 UserRole<br/>Enum"]
    end
    
    subgraph "5. Infrastructure Layer"
        DB["🗄️ PostgreSQL<br/>Relational DB"]
        CFG["⚙️ Security Config<br/>⚙️ OpenAPI Config"]
    end

    AC -->|calls| US
    UC -->|calls| US
    US -->|queries| UR
    UR -->|maps to| UE
    UE -->|contains| AE
    UE -->|references| RL
    UR -->|persists via| DB
    US -.->|uses| CFG
    
    style AC fill:#e1f5ff
    style UC fill:#e1f5ff
    style US fill:#fff3e0
    style UR fill:#f3e5f5
    style UE fill:#e8f5e9
    style AE fill:#e8f5e9
    style RL fill:#e8f5e9
    style DB fill:#fce4ec
    style CFG fill:#fce4ec
```

## 🔄 Request-Response Cycle

```mermaid
sequenceDiagram
    actor Client
    participant REST as REST Layer<br/>Controller
    participant BUS as Business Layer<br/>Service
    participant DATA as Data Access<br/>Repository
    participant DB as Database<br/>PostgreSQL

    Client->>+REST: 1. HTTP Request<br/>(JSON)
    
    REST->>REST: Validate Input<br/>(@Valid)
    
    REST->>+BUS: 2. Service Method<br/>(DTO or ID)
    
    BUS->>BUS: Business Rules<br/>Validations
    
    BUS->>+DATA: 3. Repository Query<br/>(JPA)
    
    DATA->>+DB: 4. SQL Query
    DB-->>-DATA: 5. Result Set
    
    DATA-->>-BUS: 6. Entity<br/>(User/Address)
    
    BUS->>BUS: Transform to DTO
    
    BUS-->>-REST: 7. Service Response
    
    REST->>REST: Build Response<br/>Status Code
    
    REST-->>-Client: 8. HTTP Response<br/>(JSON + Status)

    Note over Client,DB: Error Handling: GlobalExceptionHandler<br/>Returns RFC 7807 ProblemDetail
```

## 🗄️ Entity-Relationship Diagram

```mermaid
erDiagram
    USERS ||--|| ADDRESSES : "embedded"
    
    USERS {
        bigint id PK "Auto-generated"
        string login UK "Unique"
        string email UK "Unique"
        string password "BCrypt hash"
        string full_name "Required"
        enum role "RESTAURANT_OWNER or CUSTOMER"
        address address "Value Object"
        timestamp created_at "Auto-generated"
        timestamp updated_at "Auto-updated"
    }
    
    ADDRESSES {
        string street "Required"
        string number "Required"
        string complement "Optional"
        string city "Required"
        string state "Required"
        string postal_code "Required"
    }
```

## 🔐 Security & Validation Flow

```mermaid
graph TD
    A["HTTP Request"] -->|Arrives| B["Spring Dispatcher"]
    B -->|Routes to| C["Controller Method"]
    C -->|Layer 1| D["Bean Validation<br/>@Valid annotations"]
    
    D -->|Pass| E["Service Layer"]
    D -->|Fail| Z["400 Bad Request<br/>ProblemDetail"]
    
    E -->|Layer 2| F["Business Rules<br/>Email uniqueness<br/>Password complexity"]
    F -->|Pass| G["Repository Layer"]
    F -->|Fail| Y["409 Conflict<br/>ProblemDetail"]
    
    G -->|Layer 3| H["Database Constraints<br/>UNIQUE(email)<br/>UNIQUE(login)"]
    H -->|Pass| I["Record Persisted<br/>Timestamps added"]
    H -->|Fail| X["Database Error<br/>ProblemDetail"]
    
    I -->|Success| J["200/201 Response<br/>DTO Returned"]
    X --> J
    Y --> J
    Z --> J

    style D fill:#e3f2fd
    style F fill:#fff3e0
    style H fill:#f3e5f5
    style I fill:#e8f5e9
    style J fill:#c8e6c9
    style X fill:#ffcdd2
    style Y fill:#ffcdd2
    style Z fill:#ffcdd2
```

## 📁 Pacotes & Classes - Class Diagram Simplificado

```mermaid
graph TB
    subgraph config["Config Package"]
        SC["SecurityConfig"]
        OC["OpenApiConfig"]
    end
    
    subgraph controller["Controller Package"]
        AC2["AuthController"]
        UC2["UserController"]
    end
    
    subgraph service["Service Package"]
        US2["UserService"]
    end
    
    subgraph repository["Repository Package"]
        UR2["UserRepository"]
    end
    
    subgraph entity["Entity Package"]
        U["User"]
        A["Address"]
        UR3["UserRole"]
    end
    
    subgraph dto["DTO Package"]
        UCR["UserCreateRequest"]
        UUR["UserUpdateRequest"]
        UPDR["ChangePasswordRequest"]
        LVR["LoginValidateRequest"]
        UR4["UserResponse"]
        ADT["AddressDTO"]
    end
    
    subgraph exception["Exception Package"]
        GEH["GlobalExceptionHandler"]
        UNF["UserNotFoundException"]
        DUP["DuplicateEmailException"]
        INV["InvalidLoginException"]
    end

    AC2 -->|uses| US2
    UC2 -->|uses| US2
    US2 -->|uses| UR2
    UR2 -->|returns| U
    U -->|contains| A
    U -->|has role| UR3
    
    AC2 -->|receives| LVR
    UC2 -->|receives| UCR
    UC2 -->|receives| UUR
    UC2 -->|receives| UPDR
    UC2 -->|returns| UR4
    
    US2 -->|throws| UNF
    US2 -->|throws| DUP
    US2 -->|throws| INV
    GEH -->|handles| UNF
    GEH -->|handles| DUP
    GEH -->|handles| INV

    style config fill:#fce4ec
    style controller fill:#e1f5ff
    style service fill:#fff3e0
    style repository fill:#f3e5f5
    style entity fill:#e8f5e9
    style dto fill:#f1f8e9
    style exception fill:#ffebee
```

## 🧪 Test Coverage Distribution

```mermaid
pie title "Test Coverage by Component (53 tests total)"
    "UserService" : 13
    "UserController" : 8
    "E2E Integration" : 13
    "GlobalExceptionHandler" : 6
    "Entity Models" : 3
    "AuthController" : 3
    "DTOs" : 2
    "Application" : 1
    "Other" : 4
```

## 📈 Coverage by Layer

```mermaid
graph LR
    A["REST Controllers<br/>95%"]
    B["Service Layer<br/>97%"]
    C["Repository Layer<br/>92%"]
    D["Entity Models<br/>88%"]
    E["DTOs<br/>85%"]

    A -->|verified| F["Overall: 95.21%"]
    B -->|verified| F
    C -->|verified| F
    D -->|verified| F
    E -->|verified| F

    style A fill:#a5d6a7
    style B fill:#a5d6a7
    style C fill:#a5d6a7
    style D fill:#fff9c4
    style E fill:#fff9c4
    style F fill:#81c784
```

## 🚀 Deployment Flow

```mermaid
graph LR
    A["Git Push<br/>main/master"] -->|trigger| B["GitHub Actions<br/>Workflow"]
    B -->|build| C["Maven Build<br/>mvn clean install"]
    C -->|test| D["Run Tests<br/>53 tests"]
    D -->|coverage| E["JaCoCo Report<br/>95.21%"]
    E -->|success| F["Docker Build<br/>Multi-stage"]
    F -->|push| G["Docker Registry"]
    G -->|deploy| H["GitHub Pages<br/>docs/"]
    
    D -->|failure| Z["Build Failed<br/>Notify Dev"]
    C -->|failure| Z

    style A fill:#e3f2fd
    style B fill:#fff3e0
    style C fill:#f3e5f5
    style D fill:#e8f5e9
    style E fill:#c8e6c9
    style F fill:#fce4ec
    style G fill:#ffe0b2
    style H fill:#b2dfdb
    style Z fill:#ffcdd2
```

## 🔌 API Endpoints Map

```mermaid
graph TB
    API["API Base<br/>/api/v1"]
    
    API -->|Auth| AUTH["POST /auth/validate<br/>Login Validation"]
    
    API -->|Users| USERS["Users Resource<br/>/users"]
    
    USERS -->|Create| CREATE["POST /users<br/>201 Created"]
    USERS -->|Read| READ["GET /users/{id}<br/>200 OK"]
    USERS -->|Search| SEARCH["GET /users?name=xxx<br/>200 OK"]
    USERS -->|Update| UPDATE["PUT /users/{id}<br/>200 OK"]
    USERS -->|Password| PASSWORD["PATCH /users/{id}/password<br/>204 No Content"]
    USERS -->|Delete| DELETE["DELETE /users/{id}<br/>204 No Content"]

    AUTH -->|Success| S1["200 OK<br/>{message}"]
    AUTH -->|Fail| F1["401 Unauthorized<br/>ProblemDetail"]
    
    CREATE -->|Success| S2["201 Created<br/>UserResponse"]
    CREATE -->|Error| F2["400/409<br/>ProblemDetail"]
    
    READ -->|Success| S3["200 OK<br/>UserResponse"]
    READ -->|Error| F3["404 Not Found<br/>ProblemDetail"]
    
    style API fill:#e3f2fd
    style AUTH fill:#fff3e0
    style USERS fill:#f3e5f5
    style CREATE fill:#e8f5e9
    style READ fill:#e8f5e9
    style SEARCH fill:#e8f5e9
    style UPDATE fill:#e8f5e9
    style PASSWORD fill:#e8f5e9
    style DELETE fill:#e8f5e9
    style S1 fill:#c8e6c9
    style S2 fill:#c8e6c9
    style S3 fill:#c8e6c9
    style F1 fill:#ffcdd2
    style F2 fill:#ffcdd2
    style F3 fill:#ffcdd2
```

## 📦 Dependencies & Frameworks

```mermaid
graph TB
    subgraph "Core Framework"
        SB["Spring Boot 3.2.1"]
        WEB["spring-boot-starter-web"]
        SECURITY["spring-boot-starter-security"]
    end
    
    subgraph "Data Layer"
        JPA["spring-boot-starter-data-jpa"]
        HIBERNATE["Hibernate ORM"]
        POSTGRES["PostgreSQL Driver"]
    end
    
    subgraph "Validation"
        VALIDATION["spring-boot-starter-validation"]
        JAKARTA["jakarta.validation"]
    end
    
    subgraph "Documentation"
        OPENAPI["springdoc-openapi-starter"]
        SWAGGER["Swagger UI"]
    end
    
    subgraph "Testing"
        JUNIT["JUnit 5"]
        MOCKITO["Mockito 5.x"]
        TESTCONTAINERS["TestContainers"]
    end
    
    subgraph "Monitoring"
        ACTUATOR["spring-boot-starter-actuator"]
        MICROMETER["Micrometer"]
    end

    SB -->|includes| WEB
    SB -->|includes| SECURITY
    SB -->|includes| JPA
    SB -->|includes| VALIDATION
    SB -->|includes| ACTUATOR
    
    JPA -->|uses| HIBERNATE
    HIBERNATE -->|connects| POSTGRES
    
    OPENAPI -->|provides| SWAGGER
    JUNIT -->|supports| MOCKITO
    ACTUATOR -->|provides| MICROMETER

    style SB fill:#e3f2fd
    style WEB fill:#e1f5ff
    style JPA fill:#f3e5f5
    style POSTGRES fill:#fce4ec
    style SWAGGER fill:#fff3e0
    style JUNIT fill:#e8f5e9
    style MOCKITO fill:#e8f5e9
```

## 🔍 Exception Handling Strategy

```mermaid
graph TD
    REQ["Incoming Request"]
    
    REQ -->|Try| EXECUTE["Execute Handler"]
    
    EXECUTE -->|BeanValidation Error| BV["MethodArgumentNotValidException<br/>Layer 1: Input Validation"]
    EXECUTE -->|Business Logic Error| BL["Custom Exceptions<br/>Layer 2: Business Rules"]
    EXECUTE -->|Database Error| DB["DataAccessException<br/>Layer 3: Persistence"]
    EXECUTE -->|Success| OK["Return Success Response<br/>200/201/204"]
    
    BV -->|Catch| GEH["GlobalExceptionHandler"]
    BL -->|Catch| GEH
    DB -->|Catch| GEH
    
    GEH -->|Format| RFC["RFC 7807 ProblemDetail"]
    RFC -->|Return| RESP["4xx/5xx Response<br/>JSON Error Detail"]
    
    OK -->|Return| RESP

    style REQ fill:#e3f2fd
    style EXECUTE fill:#fff3e0
    style BV fill:#fff9c4
    style BL fill:#ffe0b2
    style DB fill:#ffccbc
    style GEH fill:#e1bee7
    style RFC fill:#ce93d8
    style RESP fill:#f8bbd0
    style OK fill:#c8e6c9
```

## 🧬 Domain Model Structure

```mermaid
graph TD
    subgraph "User Aggregate Root"
        USER["User<br/>────────<br/>id: Long<br/>login: String<br/>email: String<br/>password: String<br/>fullName: String<br/>role: UserRole<br/>address: Address<br/>createdAt: LocalDateTime<br/>updatedAt: LocalDateTime"]
    end
    
    subgraph "Value Objects"
        ROLE["UserRole<br/>────────<br/>RESTAURANT_OWNER<br/>CUSTOMER"]
        ADDRESS["Address<br/>────────<br/>street: String<br/>number: String<br/>complement: String<br/>city: String<br/>state: String<br/>postalCode: String"]
    end
    
    USER -->|has| ROLE
    USER -->|contains| ADDRESS
    
    style USER fill:#e8f5e9,stroke:#4caf50,stroke-width:3px
    style ROLE fill:#fff9c4,stroke:#fbc02d,stroke-width:2px
    style ADDRESS fill:#e1f5fe,stroke:#0288d1,stroke-width:2px
```

---

## 📚 Referências

- **Clean Architecture**: [The Clean Code Blog](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- **SOLID Principles**: [SOLID Explained](https://en.wikipedia.org/wiki/SOLID)
- **Spring Boot Docs**: [spring.io](https://spring.io)
- **Mermaid Diagrams**: [mermaid.js.org](https://mermaid.js.org)
