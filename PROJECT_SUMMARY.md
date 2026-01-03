# Project Summary

## Overview

Este é um projeto **Spring Boot 3** com **Java 17** que implementa uma API REST completa para **Gestão de Usuários** com arquitetura limpa e seguindo as melhores práticas do mercado.

## 📊 Estatísticas do Projeto

```
Linguagem: Java 17
Framework: Spring Boot 3.2.1
Banco de Dados: PostgreSQL 15
Build Tool: Maven
Testes: JUnit 5, Mockito
API Docs: SpringDoc OpenAPI 2.1.0
```

## 🗂️ Estrutura de Diretórios

```
fiap-tech-challenge-1/
├── src/
│   ├── main/
│   │   ├── java/com/fiap/challenge/
│   │   │   ├── controller/          # Camada de apresentação (REST)
│   │   │   ├── service/             # Camada de negócio
│   │   │   ├── repository/          # Camada de persistência
│   │   │   ├── entity/              # Entidades JPA
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exceções e tratamento global
│   │   │   ├── config/              # Configurações
│   │   │   └── UserManagementApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── init.sql
│   └── test/
│       ├── java/com/fiap/challenge/
│       │   ├── controller/          # Testes de Controller
│       │   └── service/             # Testes de Service
│       └── resources/
│           └── application-test.properties
├── pom.xml                          # Configuração Maven
├── Dockerfile                       # Imagem Docker da aplicação
├── docker-compose.yml               # Orquestração de containers
├── Makefile                         # Comandos de utilidade
├── README.md                        # Documentação principal
├── API_TESTING_GUIDE.md             # Guia de testes de API
└── CONTRIBUTING.md                  # Diretrizes de contribuição
```

## 🔌 Endpoints Implementados

### Users Management

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/users` | Criar usuário | ✅ |
| GET | `/api/v1/users/{id}` | Obter usuário por ID | ✅ |
| GET | `/api/v1/users?name=` | Buscar por nome | ✅ |
| PUT | `/api/v1/users/{id}` | Atualizar usuário | ✅ |
| PATCH | `/api/v1/users/{id}/password` | Alterar senha | ✅ |
| DELETE | `/api/v1/users/{id}` | Deletar usuário | ✅ |

### Authentication

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/auth/validate` | Validar login/senha | ✅ |

## 🏗️ Arquitetura

### Camadas

```
┌─────────────────────────────────────┐
│      REST Controllers               │  HTTP Requests/Responses
├─────────────────────────────────────┤
│      Business Logic (Services)      │  Validações e Regras de Negócio
├─────────────────────────────────────┤
│      Repository (Data Access)       │  JPA/Hibernate
├─────────────────────────────────────┤
│      PostgreSQL Database            │  Persistência
└─────────────────────────────────────┘
```

### Padrões Implementados

- ✅ **Clean Architecture**: Separação clara de responsabilidades
- ✅ **DAO/Repository Pattern**: Abstração da camada de dados
- ✅ **Service Layer**: Concentração da lógica de negócio
- ✅ **DTO Pattern**: Transferência de dados entre camadas
- ✅ **Exception Handling**: Tratamento centralizado com @RestControllerAdvice
- ✅ **Validation**: Bean Validation com mensagens customizadas
- ✅ **Security**: BCrypt para hash de senhas
- ✅ **API Documentation**: OpenAPI/Swagger

## 🔐 Segurança

- **Autenticação**: Validação de login/senha com BCrypt
- **Criptografia**: BCrypt com strength 12
- **Sanitização**: Validação em todos os inputs
- **Proteção**: Nunca expõe senhas ou hashes em responses
- **Transações**: Isolamento com @Transactional
- **Status HTTP**: Códigos apropriados (400, 401, 404, 409)

## 🧪 Testes

### Cobertura

- ✅ **Unit Tests**: UserService, UserController, AuthController
- ✅ **Mock Tests**: Mockito para dependências externas
- ✅ **Integration Tests**: Configuração com @SpringBootTest

### Executar Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=UserServiceTest

# Com cobertura
mvn test jacoco:report
```

## 📦 Dependências Principais

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>    <!-- REST -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId> <!-- ORM -->
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>                  <!-- DB Driver -->
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>      <!-- BCrypt -->
</dependency>

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId> <!-- Swagger -->
</dependency>
```

## 🚀 Como Começar

### Opção 1: Docker Compose (Recomendado)

```bash
docker-compose up --build
# Acesse: http://localhost:8080/swagger-ui.html
```

### Opção 2: Executar Localmente

```bash
# Pré-requisitos: PostgreSQL rodando em localhost:5432

# Compilar
mvn clean compile

# Executar testes
mvn test

# Rodar aplicação
mvn spring-boot:run

# Acesse: http://localhost:8080/swagger-ui.html
```

## 📚 Documentação

- **[README.md](README.md)** - Documentação principal
- **[API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)** - Exemplos de requisições
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Guia para contribuidores
- **[Swagger UI](http://localhost:8080/swagger-ui.html)** - Documentação interativa

## 🔍 Monitoramento

```
Health Check: http://localhost:8080/actuator/health
Info: http://localhost:8080/actuator/info
Metrics: http://localhost:8080/actuator/metrics
API Docs: http://localhost:8080/v3/api-docs
Swagger UI: http://localhost:8080/swagger-ui.html
```

## 🛠️ Ferramentas Úteis

### Makefile

```bash
make build              # Compilar
make test              # Testes
make run               # Executar localmente
make docker-up         # Iniciar containers
make docker-down       # Parar containers
make swagger           # Abrir Swagger UI
```

## 📋 Checklist de Funcionalidades

### Entidades
- ✅ User com campos completos
- ✅ Address embarcado
- ✅ UserRole enum
- ✅ Timestamps (createdAt, lastModifiedAt)
- ✅ Validações com Bean Validation

### DTOs
- ✅ UserCreateRequest (com senha)
- ✅ UserUpdateRequest (sem senha)
- ✅ ChangePasswordRequest
- ✅ LoginValidateRequest
- ✅ UserResponse (sem exposição de senha)
- ✅ AddressDTO

### Endpoints
- ✅ POST /api/v1/users (201)
- ✅ GET /api/v1/users/{id}
- ✅ GET /api/v1/users?name= (busca)
- ✅ PUT /api/v1/users/{id}
- ✅ PATCH /api/v1/users/{id}/password
- ✅ DELETE /api/v1/users/{id}
- ✅ POST /api/v1/auth/validate

### Tratamento de Erros
- ✅ 400 Bad Request (validação)
- ✅ 401 Unauthorized (login inválido)
- ✅ 404 Not Found (usuário não existe)
- ✅ 409 Conflict (email duplicado)
- ✅ ProblemDetail (RFC 7807)

### Segurança
- ✅ BCrypt com strength 12
- ✅ Validação de entrada
- ✅ Unicidade de email/login
- ✅ Senhas nunca expostas

### Infraestrutura
- ✅ Dockerfile multi-stage
- ✅ docker-compose com PostgreSQL
- ✅ Health check
- ✅ Logs estruturados

### Documentação
- ✅ SpringDoc OpenAPI/Swagger
- ✅ JavaDoc em métodos públicos
- ✅ README completo
- ✅ Guia de testes
- ✅ Diretrizes de contribuição

## 🔄 CI/CD Pronto Para

- ✅ GitHub Actions
- ✅ GitLab CI
- ✅ Jenkins
- ✅ Azure DevOps

## 📄 Licença

Apache 2.0

## 👥 Contribuições

Veja [CONTRIBUTING.md](CONTRIBUTING.md) para diretrizes.

---

**Desenvolvido com ❤️ para FIAP Tech Challenge**
