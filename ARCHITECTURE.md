# FIAP Tech Challenge - Phase 1: Arquitetura e Desenvolvimento

> 📊 **Veja os diagramas visuais em [DIAGRAMS.md](DIAGRAMS.md)**

## 📋 Visão Geral

Sistema de **gerenciamento de usuários para restaurantes** implementado com **Spring Boot 3.2.1**, **Java 17** e **PostgreSQL 15**, seguindo **Clean Architecture**, **SOLID Principles** e boas práticas de desenvolvimento backend.

### Requisitos Implementados

✅ **Tipos de Usuário**: RESTAURANT_OWNER, CUSTOMER  
✅ **CRUD Completo**: Create, Read, Update, Delete  
✅ **Endpoints Exclusivos**: Troca de senha + Atualização de dados (sem senha)  
✅ **Autenticação Stateless**: Validação de login + senha (sem Spring Security obrigatório)  
✅ **Busca Avançada**: Por nome com busca case-insensitive  
✅ **Unicidade de Email**: Garantida em nível de banco e validação  
✅ **Auditoria**: Data de criação e última alteração automáticas  
✅ **API Versionada**: `/api/v1`  
✅ **Tratamento de Erros**: RFC 7807 (ProblemDetail)  
✅ **Documentação**: Swagger/OpenAPI com exemplos  
✅ **Código**: SOLID, OOP, Clean Architecture  
✅ **Infraestrutura**: Docker + Docker Compose  
✅ **Testes**: 53 testes (95.21% cobertura)  

---

## 🏗️ Arquitetura

### Camadas (5 Layers - Clean Architecture)

```
┌─────────────────────────────────────┐
│  REST API Layer                     │
│  ├─ AuthController                  │
│  └─ UserController                  │
├─────────────────────────────────────┤
│  Business Logic Layer               │
│  └─ UserService                     │
├─────────────────────────────────────┤
│  Data Access Layer                  │
│  ├─ UserRepository (JPA)            │
│  └─ Database Integration            │
├─────────────────────────────────────┤
│  Domain Layer                       │
│  ├─ User (Entity)                   │
│  ├─ Address (Value Object)          │
│  └─ UserRole (Enum)                 │
├─────────────────────────────────────┤
│  Infrastructure                     │
│  ├─ PostgreSQL 15                   │
│  ├─ JPA/Hibernate ORM               │
│  ├─ BCrypt Password Encoder         │
│  └─ Exception Handlers              │
└─────────────────────────────────────┘
```

### Padrões de Design Utilizados

- **Repository Pattern**: `UserRepository` abstrai acesso a dados
- **Service Layer**: `UserService` concentra lógica de negócio
- **DTO Pattern**: `UserCreateRequest`, `UserUpdateRequest`, `UserResponse`
- **Factory Pattern**: Construção de `UserResponse` a partir de `User`
- **Dependency Injection**: Via construtor (Spring)
- **Exception Handling**: `GlobalExceptionHandler` com RFC 7807

---

## 🔄 Evolução Arquitetural: Clean Architecture + DDD

Foi aplicada uma evolução incremental para reduzir acoplamento entre regras de negócio e detalhes de persistência.

### Estrutura aplicada

```
Controller (entrada HTTP)
  -> Application Use Cases (orquestração de regras)
    -> Domain Port (contrato de saída)
      -> Infrastructure Adapter JPA (implementação técnica)
        -> Spring Data Repository
```

### Bounded Contexts aplicados

- `usertype`: implementado com Domain + Application + Infrastructure
- `restaurant`: implementado com Domain + Application + Infrastructure
- `menuitem`: implementado com Domain + Application + Infrastructure
- `user` e `auth`: transição gradual com orchestrators de use case na camada Application

### Novos pacotes

- `domain.usertype.model`: modelo de domínio (`UserTypeAggregate`)
- `domain.usertype.port`: porta de saída (`UserTypeGateway`)
- `application.usertype.usecase`: casos de uso explícitos
- `application.usertype.dto`: comandos e resultado de aplicação
- `infrastructure.persistence.usertype`: adapter JPA (`UserTypeJpaGateway`)

### Decisão arquitetural: DTO vs Model

- `DTO`: usado nas bordas do sistema (HTTP request/response) para contrato de API e validação.
- `Model de domínio (Aggregate)`: usado no núcleo da aplicação para representar regra de negócio e estado.
- `Implementação adotada`: controllers continuam recebendo/retornando DTOs, e use cases operam com modelos internos + mapeadores.

Essa separação evita vazamento de detalhes de persistência/API para o domínio e melhora testabilidade e evolução independente das camadas.

### Ganhos obtidos

- Casos de uso isolados e testáveis sem dependência direta de JPA.
- Regra de negócio dependente de abstração (`UserTypeGateway`), não de framework.
- Controller desacoplado do service legado e orientado a caso de uso.
- Base pronta para expandir o mesmo padrão para `Restaurant`, `MenuItem` e `User`.

---

## 📁 Estrutura de Pastas

```
fiap-tech-challenge-1/
├── src/
│   ├── main/java/com/fiap/challenge/
│   │   ├── UserManagementApplication.java
│   │   ├── config/
│   │   │   ├── OpenApiConfig.java          # Swagger/OpenAPI
│   │   │   └── SecurityConfig.java         # Spring Security + BCrypt
│   │   ├── controller/
│   │   │   ├── AuthController.java         # POST /api/v1/auth/validate
│   │   │   └── UserController.java         # 6 endpoints CRUD
│   │   ├── service/
│   │   │   └── UserService.java            # 8 métodos de negócio
│   │   ├── repository/
│   │   │   └── UserRepository.java         # JPA com queries customizadas
│   │   ├── entity/
│   │   │   ├── User.java                   # Entity com validações
│   │   │   ├── Address.java                # Value Object Embedded
│   │   │   └── UserRole.java               # Enum: RESTAURANT_OWNER, CUSTOMER
│   │   ├── dto/
│   │   │   ├── UserCreateRequest.java
│   │   │   ├── UserUpdateRequest.java
│   │   │   ├── UserResponse.java
│   │   │   ├── AddressDTO.java
│   │   │   ├── ChangePasswordRequest.java
│   │   │   └── LoginValidateRequest.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       ├── UserNotFoundException.java
│   │       ├── DuplicateEmailException.java
│   │       └── InvalidLoginException.java
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── init.sql                        # Script de inicialização
│   └── test/java/com/fiap/challenge/
│       ├── IntegrationTest.java            # Anotação customizada
│       ├── UserManagementApplicationTest.java
│       ├── controller/
│       │   ├── UserControllerTest.java     # 8 testes
│       │   └── AuthControllerTest.java     # 3 testes
│       ├── service/
│       │   └── UserServiceTest.java        # 13 testes
│       ├── exception/
│       │   └── GlobalExceptionHandlerTest.java  # 6 testes
│       ├── entity/
│       │   ├── UserEntityTest.java
│       │   └── UserRoleTest.java
│       ├── integration/
│       │   └── UserManagementE2ETest.java  # 13 testes E2E
│       └── dto/
│           └── UserResponseTest.java
├── pom.xml                                  # Maven: 13 dependências
├── Dockerfile                               # Multi-stage, non-root user
├── docker-compose.yml                       # App + PostgreSQL 15
├── .env.example                             # Variáveis de ambiente
├── postman_collection.json                  # 18 requisições
└── README.md
```

---

## 🔌 API Endpoints

**Base URL:** `http://localhost:8080/api/v1`

### Autenticação

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/auth/validate` | Valida login + senha | 200/401 |

### Usuários (CRUD)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/users` | Criar usuário | 201 Created |
| GET | `/users/{id}` | Buscar por ID | 200 OK |
| GET | `/users?name=xxx` | Buscar por nome | 200 OK |
| PUT | `/users/{id}` | Atualizar (sem senha) | 200 OK |
| PATCH | `/users/{id}/password` | Alterar senha | 204 No Content |
| DELETE | `/users/{id}` | Remover usuário | 204 No Content |

---

## 📊 Modelagem de Dados

### Entidade: User

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  login VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  street VARCHAR(255),
  number VARCHAR(10),
  complement VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(2),
  zip_code VARCHAR(10),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Validações

**User Entity:**
- `name`: Obrigatório, max 150 chars
- `email`: Obrigatório, padrão RFC, UNIQUE
- `login`: Obrigatório, UNIQUE
- `password`: Obrigatório, codificado com BCrypt (strength 12)
- `role`: Obrigatório, enum (RESTAURANT_OWNER, CUSTOMER)
- `address`: Opcional, Value Object Embedded

**Address Value Object:**
- `street`: Obrigatório
- `number`: Obrigatório
- `complement`: Opcional
- `city`: Obrigatório
- `state`: Obrigatório (2 chars)
- `zipCode`: Obrigatório

---

## 🧪 Testes

### Cobertura: 95.21% (369/387 linhas)

**Testes Unitários (17)**
- `UserServiceTest`: 13 testes
- `UserRoleTest`: 1 teste
- `UserResponseTest`: 2 testes
- `UserManagementApplicationTest`: 1 teste

**Testes de Integração (36)**
- `UserControllerTest`: 8 testes
- `AuthControllerTest`: 3 testes
- `UserManagementE2ETest`: 13 testes (fluxos completos)
- `GlobalExceptionHandlerTest`: 6 testes
- `UserEntityTest`: 1 teste

### Executar

```bash
# Todos os testes
mvn test

# Com cobertura JaCoCo
mvn verify

# Gerar relatório
mvn jacoco:report
# Abrir: target/site/jacoco/index.html
```

---

## 🔐 Segurança

### Autenticação
- Validação **stateless** de login + senha
- Sem tokens JWT nesta fase
- BCrypt com strength 12 (~14 hashes/segundo)
- Senha nunca exposta em responses

### Criptografia
- **BCrypt 12** para hashes
- Configurado em `SecurityConfig.passwordEncoder()`
- Injetado na `UserService` via constructor

### Validação (3 camadas)
1. **Bean Validation**: `@NotBlank`, `@Email`, etc.
2. **Service**: Regras de negócio (duplicatas, conformidade)
3. **Database**: Constraints SQL (UNIQUE, NOT NULL)

### Proteção de Dados
- Email validado com regex RFC
- Senha verificada com `passwordEncoder.matches()`
- Campos sensíveis excluídos de responses

### HTTP Status
- `201` – Recurso criado
- `204` – Sucesso sem corpo
- `400` – Validação falhou
- `401` – Credenciais inválidas
- `404` – Recurso não encontrado
- `409` – Conflito (email duplicado)

---

## 📈 Principais Classes

### Entity: User.java

```java
@Entity
@Table(name = "users", uniqueConstraints = {
  @UniqueConstraint(columnNames = "email"),
  @UniqueConstraint(columnNames = "login")
})
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotBlank
  @Column(nullable = false, length = 150)
  private String name;
  
  @Email @NotBlank
  @Column(nullable = false, unique = true)
  private String email;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole role;
  
  @Embedded @Valid
  private Address address;
  
  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime lastModifiedAt;
}
```

### Service: UserService.java

```java
@Service
@Transactional
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  
  // 8 métodos públicos:
  public UserResponse createUser(UserCreateRequest) {}
  public UserResponse getUserById(Long id) {}
  public List<UserResponse> getUsersByName(String name) {}
  public UserResponse updateUser(Long id, UserUpdateRequest) {}
  public void changePassword(Long id, ChangePasswordRequest) {}
  public void deleteUser(Long id) {}
  public void validateLogin(LoginValidateRequest) {}
}
```

### Controller: UserController.java

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários")
public class UserController {
  // 6 endpoints com Swagger annotations
  @PostMapping
  @PutMapping("/{id}")
  @PatchMapping("/{id}/password")
  @GetMapping("/{id}")
  @GetMapping (busca por name)
  @DeleteMapping("/{id}")
}
```

### Exception Handler: GlobalExceptionHandler.java

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
  // Trata 6 tipos de exceções
  // Retorna ProblemDetail (RFC 7807) com:
  // - errorCode (VALIDATION_FAILED, DUPLICATE_EMAIL, etc.)
  // - validationErrors (map de campo -> erro)
  // - traceId (UUID)
  // - timestamp
  // - severity (error, critical)
}
```

---

## 🚀 Execução

### Via Docker (Recomendado)

```bash
docker-compose up --build
```

Acesse:
- **API**: http://localhost:8080/api/v1
- **Swagger**: http://localhost:8080/swagger-ui.html
- **Health**: http://localhost:8080/actuator/health
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Localmente

```bash
# 1. Instale PostgreSQL 15
# 2. Crie banco de dados
createdb user_management

# 3. Configure application.properties (ou use padrões)
# spring.datasource.url=jdbc:postgresql://localhost:5433/user_management

# 4. Compile
mvn clean install

# 5. Execute
mvn spring-boot:run
```

---

## 📦 Dependências Principais

| Dependência | Versão | Propósito |
|-------------|--------|----------|
| spring-boot-starter-web | 3.2.1 | REST API |
| spring-boot-starter-data-jpa | 3.2.1 | ORM |
| spring-boot-starter-validation | 3.2.1 | Bean Validation |
| spring-security-crypto | - | BCrypt |
| postgresql | 42.7.1 | Driver DB |
| springdoc-openapi-starter-webmvc-ui | 2.1.0 | Swagger |
| h2database | - | Testes (in-memory) |
| junit-jupiter | 5.x | JUnit 5 |
| mockito | 5.x | Mocks |

---

## 📐 SOLID Principles Aplicados

✅ **S - Single Responsibility**: Cada classe tem uma responsabilidade única  
✅ **O - Open/Closed**: Aberto para extensão (ex: novos roles), fechado para modificação  
✅ **L - Liskov Substitution**: UserResponse substitui User sem alterar comportamento  
✅ **I - Interface Segregation**: Interfaces específicas (UserRepository)  
✅ **D - Dependency Inversion**: Dependência injetada via constructor  

---

## 📚 Postman Collection

**18 requisições** cobrindo:
- ✅ Sucesso em todos os fluxos
- ❌ Erros de validação (email inválido, campos vazios)
- ❌ Erros de negócio (email duplicado, usuário não encontrado)
- ❌ Erros de autenticação (credenciais inválidas)

**Arquivo**: `postman_collection.json`

---

## 🔧 Configuração

### application.properties

```properties
# Server
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/user_management
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Variáveis de Ambiente

Crie `.env` (não commitar) com:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/user_management
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

---

## 📋 Checklist de Requisitos

- ✅ Sistema de gerenciamento de usuários para restaurantes
- ✅ Tipos: RESTAURANT_OWNER, CUSTOMER
- ✅ CRUD completo
- ✅ Endpoint exclusivo de troca de senha
- ✅ Endpoint exclusivo de atualização de dados (sem senha)
- ✅ Validação de login (login + senha) sem Spring Security obrigatório
- ✅ Busca de usuários por nome
- ✅ Garantia de unicidade de email
- ✅ Registro automático de data da última alteração
- ✅ Java + Spring Boot (camadas: controller, service, repository, domain)
- ✅ Banco de dados relacional (PostgreSQL 15)
- ✅ JPA/Hibernate
- ✅ API versionada (/api/v1)
- ✅ Tratamento de erros (RFC 7807 - ProblemDetail)
- ✅ Swagger/OpenAPI com exemplos
- ✅ Código SOLID + boas práticas + OOP
- ✅ Docker + docker-compose
- ✅ README.md único e central
- ✅ Coleção Postman com exemplos de sucesso e erro

---

## 🎯 Próximos Passos (Futuro)

- JWT tokens para autenticação stateful
- Refresh tokens
- Rate limiting
- Logging centralizador (ELK stack)
- Métricas (Prometheus)
- CI/CD pipeline
- Testes de carga
- Cache distribuído (Redis)

---

## 📞 Suporte

Consulte o [README.md](README.md) para instruções rápidas.

Documentação interativa em: http://localhost:8080/swagger-ui.html

---

**Desenvolvido para FIAP Tech Challenge – Phase 1 (Arquitetura e Desenvolvimento Java)**  
**Status:** ✅ Production-Ready | 🧪 95.21% Test Coverage | 📚 Fully Documented
