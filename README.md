# FIAP Tech Challenge – Phase 1: User Management API

**REST API for User Management | Spring Boot 3.2.1 | Java 17 | PostgreSQL 15**

---

## �️ Documentation Map

| Document | Purpose | Audience |
|----------|---------|----------|
| [README.md](README.md) | **Overview & quick reference** | Everyone |
| [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) | **Complete technical report** (academic level) | Evaluators, architects |
| [QUICK_START.md](QUICK_START.md) | **5-minute setup guide** | New developers |
| [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) | **HTTP request examples** | API consumers, QA |
| [BEST_PRACTICES.md](BEST_PRACTICES.md) | **Engineering patterns & principles** | Developers |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | **Common issues & solutions** | Everyone |
| [CONTRIBUTING.md](CONTRIBUTING.md) | **Contribution guidelines** | Contributors |

---

## �📋 Overview

Backend system implementing a RESTful API for user management in **Spring Boot 3.2.1** with **Java 17** and **PostgreSQL 15**. Achieves **95.21% test coverage** with Clean Architecture and production-ready quality.

**Key Metrics:**
- ✅ 18/18 requirements implemented (100%)
- ✅ 53 tests passing (17 unit + 36 integration)
- ✅ 95.21% code coverage (JaCoCo)
- ✅ 7 REST endpoints (RFC 7807 compliant)
- ✅ Clean Architecture + SOLID principles

---

## 🛠️ Technology Stack

| Layer | Technologies |
|-------|---|
| **Language** | Java 17 LTS |
| **Framework** | Spring Boot 3.2.1 |
| **Web** | Spring Web, Spring Validation (JSR-380) |
| **Data** | Spring Data JPA, Hibernate, PostgreSQL 15, H2 (tests) |
| **Security** | BCrypt (strength 12) |
| **Testing** | JUnit 5, Mockito, Spring Test |
| **Quality** | JaCoCo (95.21% coverage) |
| **API Docs** | SpringDoc OpenAPI 2.1.0 (Swagger UI) |
| **Build** | Maven 3.9.6 |
| **DevOps** | Docker multi-stage, Docker Compose |

---

## 🏗️ Architecture at a Glance

**Clean Architecture (5 layers):**

```
REST API Layer         → 2 Controllers (User + Auth)
   ↓
Business Logic Layer   → UserService (9 methods)
   ↓
Data Access Layer      → UserRepository (JPA)
   ↓
Domain Layer           → 3 Entities + UserRole enum
   ↓
Database               → PostgreSQL 15
```

**Design Patterns:** Repository, Service, DTO, Factory, Dependency Injection  
**Principles:** SOLID, Clean Code, DRY, KISS

---

## 🔌 API Endpoints

**Base URL:** `http://localhost:8080/api/v1`

| Method | Endpoint | Purpose | Status |
|--------|----------|---------|--------|
| POST | `/auth/validate` | Validate credentials | 200/401 |
| POST | `/users` | Create user | 201 Created |
| GET | `/users/{id}` | Get user by ID | 200 OK |
| GET | `/users?name=` | Search by name | 200 OK |
| PUT | `/users/{id}` | Update user (no password) | 200 OK |
| PATCH | `/users/{id}/password` | Change password | 204 No Content |
| DELETE | `/users/{id}` | Delete user | 204 No Content |

**Error Handling:** RFC 7807 (ProblemDetail) with standardized error responses.

---

## 🚀 Quick Start

### Via Docker (Recommended)

```bash
docker-compose up --build
# Swagger UI: http://localhost:8080/swagger-ui.html
# Health: http://localhost:8080/actuator/health
```

### Local Setup

```bash
# Prerequisites: Java 17, Maven 3.6, PostgreSQL 15
createdb user_management
mvn clean install
mvn spring-boot:run
```

---

## 🧪 Testing & Quality

- **53 Tests Total:** 17 unit + 36 integration tests
- **Coverage:** 95.21% line coverage (369 covered / 18 uncovered)
- **Validation:** 3-layer approach (Bean Validation → Service → Database)
- **Security:** BCrypt hashing, SQL injection prevention, CSRF protection
- **Run tests:** `mvn clean verify`

---

## 📚 Documentation

| Document | Purpose |
|----------|---------|
| [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) | Academic technical report (scientific level) |
| [QUICK_START.md](QUICK_START.md) | 5-minute quick start guide |
| [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) | HTTP request examples with cURL and Postman |
| [BEST_PRACTICES.md](BEST_PRACTICES.md) | Engineering patterns and principles implemented |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | Common issues and solutions |
| [CONTRIBUTING.md](CONTRIBUTING.md) | Contribution guidelines and code standards |

**Swagger UI:** `http://localhost:8080/swagger-ui.html`  
**Postman Collection:** `postman_collection.json`
| `GET /v3/api-docs` | OpenAPI JSON |
| `GET /swagger-ui.html` | Documentação interativa |

---

## 📊 Modelagem de Dados

### Tabela: users

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    address_street VARCHAR(255),
    address_number VARCHAR(10),
    address_complement VARCHAR(255),
    address_city VARCHAR(100),
    address_state VARCHAR(2),
    address_zip_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Campos e Validações

| Campo | Tipo | Validação | Nota |
|-------|------|-----------|------|
| `id` | BIGINT | PK, Auto-increment | — |
| `name` | String | @NotBlank, max 150 | Obrigatório |
| `email` | String | @Email, UNIQUE | Regex RFC |
| `login` | String | @NotBlank, UNIQUE | Obrigatório |
| `password_hash` | String | BCrypt (strength 12) | Nunca exposto |
| `role` | Enum | RESTAURANT_OWNER, CUSTOMER | Obrigatório |
| `address` | Embedded | @Valid, validações internas | Opcional |
| `created_at` | LocalDateTime | @CreationTimestamp | Imutável |
| `last_modified_at` | LocalDateTime | @UpdateTimestamp | Automático |

---

## 🔐 Segurança

### Autenticação
- Validação de login/senha sem estado (stateless)
- Resposta padronizada (200 ou 401)
- Sem tokens JWT nesta fase
- **Actuator endpoints protegidos com HTTP Basic** (role: ACTUATOR_ADMIN)

### Criptografia
- **BCrypt com strength 12** – ~14 hashes/segundo
- Nunca expõe `passwordHash` em responses
- Senha atualizada em endpoint exclusivo

### Validação
- **3 camadas**: Bean Validation (@NotBlank, @Email) + Service (regras negócio) + Database (constraints)
- Inputs sanitizados antes de processar
- Erro 409 para conflitos (email duplicado)

### Proteção de Endpoints de Gerenciamento
- **Actuator endpoints restritos:** Apenas `/actuator/health` exposto publicamente (status básico)
- **Detalhes sensíveis ocultos:** `show-details=when-authorized` (requer autenticação)
- **Autenticação obrigatória:** HTTP Basic com credenciais via variáveis de ambiente
- 📖 **Ver seção "Segurança: Actuator Endpoints"** em [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### Status HTTP
- **201** – Recurso criado
- **204** – Sucesso sem resposta
- **400** – Validação falhou
- **401** – Credenciais inválidas
- **404** – Recurso não encontrado
- **409** – Conflito (email duplicado)

---

## 🧪 Testes

### Cobertura: 95.21% ✅

```
Linhas cobertas: 369
Linhas não cobertas: 18
Total: 387 linhas
```

### Testes Implementados (53 total)

**Unit Tests (17)**
- `UserServiceTest` – 13 testes
- `UserRoleTest` – 1 teste
- `UserResponseTest` – 2 testes
- `UserManagementApplicationTest` – 1 teste

**Integration Tests (36)**
- `UserControllerTest` – 13 testes
- `AuthControllerTest` – 3 testes
- `UserManagementE2ETest` – 13 testes
- `GlobalExceptionHandlerTest` – 6 testes
- `UserEntityTest` – 1 teste

### Executar Testes

```bash
# Todos os testes com cobertura
mvn clean verify

# Apenas testes
mvn test

# Gerar relatório JaCoCo
mvn jacoco:report
# Acesse: target/site/jacoco/index.html
```

---

## 🚀 Como Começar

### ⚡ Opção 1: Docker (Recomendado)

```bash
# 1. Clonar ou acessar o projeto
cd fiap-tech-challenge-1

# 2. Iniciar containers
docker-compose up --build

# 3. Aguarde 2-3 minutos até ver:
# "Started UserManagementApplication in X seconds"

# 4. Acesse
# Swagger UI: http://localhost:8080/swagger-ui.html
# Health: http://localhost:8080/actuator/health
```

### 💻 Opção 2: Localmente

**Pré-requisitos:**
- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 13+ (rodando em localhost:5432)

```bash
# 1. Criar banco de dados
psql -U postgres -c "CREATE DATABASE user_management;"

# 2. Compilar e rodar
mvn clean install
mvn spring-boot:run

# 3. Acesse
# http://localhost:8080/swagger-ui.html
```

### 🔍 Verificar Status

```bash
# Health check (público - status básico apenas)
curl http://localhost:8080/actuator/health

# Health check detalhado (requer autenticação)
curl -u admin:your_password http://localhost:8080/actuator/health

# ⚠️ Info e metrics endpoints não são mais públicos por motivos de segurança
# Configure credenciais em .env para acesso a endpoints protegidos
```

**Nota de Segurança:** Por padrão, apenas o endpoint `/actuator/health` está exposto com informações básicas. Para acessar detalhes completos ou outros endpoints do Actuator, configure as credenciais conforme descrito em [TROUBLESHOOTING.md](TROUBLESHOOTING.md) (seção "Segurança: Actuator Endpoints").

---

## 📚 Exemplos de Requisições

### Criar Usuário

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "password": "Senha@123",
    "role": "CUSTOMER",
    "address": {
      "street": "Rua A",
      "number": "123",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01234-567"
    }
  }'
```

### Validar Credenciais

```bash
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "Senha@123"
  }'
```

### Alterar Senha

```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "Senha@123",
    "newPassword": "NovaSenh@456",
    "confirmPassword": "NovaSenh@456"
  }'
```

### Buscar por Nome

```bash
curl -X GET "http://localhost:8080/api/v1/users?name=João" \
  -H "Content-Type: application/json"
```

---

## 📖 Documentação Complementar

| Documento | Propósito |
|-----------|-----------|
| [QUICK_START.md](QUICK_START.md) | Começar em 5 minutos |
| [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) | Exemplos detalhados de requisições |
| [BEST_PRACTICES.md](BEST_PRACTICES.md) | Padrões e práticas implementadas |
| [CONTRIBUTING.md](CONTRIBUTING.md) | Como contribuir com o projeto |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | FAQ e soluções de problemas |
| [TECHNICAL_REPORT.md](TECHNICAL_REPORT.md) | Relatório técnico completo |

---

## 📈 Métricas de Qualidade

| Métrica | Valor |
|---------|-------|
| **Cobertura de Testes (JaCoCo)** | 95.21% ✅ |
| **Testes Totais** | 53 |
| **Testes Unitários** | 17 |
| **Testes de Integração** | 36 |
| **Endpoints Implementados** | 7 |
| **Arquivos Java** | 25+ |
| **DTOs** | 6 |
| **Exceções Customizadas** | 3 |
| **Tempo Build Docker** | ~2-3 min |
| **Tamanho Imagem Docker** | ~400MB |

---

## 🎓 Padrões de Engenharia

### Clean Architecture
✅ Separação clara de responsabilidades  
✅ Independência entre camadas  
✅ Fácil testabilidade  
✅ Escalabilidade

### SOLID Principles
✅ **S** – Single Responsibility: cada classe tem uma responsabilidade  
✅ **O** – Open/Closed: aberto para extensão, fechado para modificação  
✅ **L** – Liskov: subtypes substituem tipos base  
✅ **I** – Interface Segregation: interfaces específicas  
✅ **D** – Dependency Inversion: depender de abstrações  

### Design Patterns
✅ Repository Pattern – abstração de dados  
✅ Service Layer – concentração de lógica  
✅ DTO Pattern – transferência de dados  
✅ Factory Pattern – criação de objetos  
✅ Observer Pattern – eventos Spring  

### RFC & Padrões
✅ **RFC 7807** – ProblemDetail para erros HTTP  
✅ **RESTful** – HTTP verbs e status codes apropriados  
✅ **OpenAPI 3.0** – Documentação de API  
✅ **Semantic Versioning** – API /v1  

---

## 🔄 CI/CD Ready

- ✅ Maven configurado para testes automáticos
- ✅ JaCoCo para cobertura de testes
- ✅ Dockerfile otimizado para CI/CD
- ✅ docker-compose para environment local
- ✅ Health checks integrados
- ✅ Logs estruturados

---

## 📦 Dependências Principais

```xml
<!-- Spring Boot -->
<spring-boot-starter-web>           <!-- REST -->
<spring-boot-starter-data-jpa>      <!-- ORM -->
<spring-boot-starter-validation>    <!-- Bean Validation -->
<spring-security-crypto>            <!-- BCrypt -->

<!-- Database -->
<postgresql>                         <!-- Driver -->
<h2database>                         <!-- Testes -->

<!-- Documentation -->
<springdoc-openapi-starter-webmvc-ui>  <!-- Swagger -->

<!-- Testing -->
<spring-boot-starter-test>          <!-- JUnit 5, Mockito -->
```

---

## 🎯 Próximos Passos Sugeridos

1. **Explorar a API**
   ```bash
   docker-compose up --build
   # Acesse http://localhost:8080/swagger-ui.html
   ```

2. **Ler Documentação Rápida**
   - Consulte [QUICK_START.md](QUICK_START.md)

3. **Testar Endpoints**
   - Use [postman_collection.json](postman_collection.json)
   - Ou exemplos em [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)

4. **Entender Arquitetura**
   - Veja [TECHNICAL_REPORT.md](TECHNICAL_REPORT.md)

5. **Contribuir**
   - Siga [CONTRIBUTING.md](CONTRIBUTING.md)

---

## 📋 Checklist de Produção

- ✅ Arquitetura em camadas (5 camadas)
- ✅ Todos endpoints implementados (7)
- ✅ Validações em 3 camadas
- ✅ Tratamento centralizado de erros (RFC 7807)
- ✅ Testes completos (95.21% cobertura)
- ✅ Documentação profissional
- ✅ Docker & Docker Compose
- ✅ Health checks
- ✅ Logging estruturado
- ✅ Segurança (BCrypt, validações)
- ✅ SOLID Principles aplicados
- ✅ Pronto para produção

---

## 📞 Suporte

**Problemas comuns?** Consulte [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

**Dúvidas sobre implementação?** Veja [BEST_PRACTICES.md](BEST_PRACTICES.md)

**Quer contribuir?** Leia [CONTRIBUTING.md](CONTRIBUTING.md)

---

## 📄 Licença

MIT License – 2026

---

**Desenvolvido para FIAP Tech Challenge – Fase 1**  
**Status:** ✅ Produção-Ready | 🧪 95.21% Cobertura | 📚 Documentado
