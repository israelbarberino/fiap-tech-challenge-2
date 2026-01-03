# 👥 FIAP Tech Challenge - Fase 1: Gestão de Usuários

## 📋 Visão Geral do Projeto

Este projeto implementa um sistema completo de **Gestão de Usuários** para a **Fase 1** do FIAP Tech Challenge. A aplicação fornece um conjunto robusto de APIs RESTful para criar, atualizar, consultar e deletar usuários, com suporte a autenticação simples e tratamento de erros padronizado.

### 🎯 Objetivo

Desenvolver uma aplicação Spring Boot 3 moderna, escalável e bem documentada que atenda todos os requisitos técnicos do desafio, priorizando clareza, segurança e boas práticas de desenvolvimento.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17 LTS** - Linguagem de programação
- **Spring Boot 3.2.1** - Framework principal
- **Spring Web** - APIs REST
- **Spring Data JPA** - ORM com Hibernate
- **Spring Validation** - Bean Validation (JSR-380)
- **PostgreSQL 15** - Banco de dados relacional
- **SpringDoc OpenAPI 2.1.0** - Geração automática de Swagger UI

### Ferramentas & Build
- **Maven 3.9.6** - Gerenciador de dependências
- **Docker** - Containerização
- **Docker Compose** - Orquestração local

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking
- **Spring Test** - Integração com Spring

---

## 🏗️ Arquitetura da Solução

### Padrão em Camadas (Layered Architecture)

```
┌─────────────────────────────────────┐
│     Controller Layer (REST API)     │
├─────────────────────────────────────┤
│     Service Layer (Business Logic)  │
├─────────────────────────────────────┤
│    Repository Layer (Data Access)   │
├─────────────────────────────────────┤
│     Entity & Domain Models          │
├─────────────────────────────────────┤
│      PostgreSQL Database            │
└─────────────────────────────────────┘
```

### Componentes Principais

#### 1. **Controllers** (`controller/`)
- `UserController.java` - Endpoints para CRUD de usuários
- `AuthController.java` - Validação de credenciais
- Documentados com SpringDoc OpenAPI (@Operation, @ApiResponse)

#### 2. **Services** (`service/`)
- `UserService.java` - Lógica de negócio
- Validação de dados
- Codificação BCrypt de senhas
- Transações gerenciadas (@Transactional)

#### 3. **Repositories** (`repository/`)
- `UserRepository.java` - Acesso a dados com JPA
- Queries customizadas (findByEmail, findByLogin, etc.)

#### 4. **Entities** (`entity/`)
- `User.java` - Entidade JPA com mapeamento relacional
- `UserRole.java` - Enum de papéis (RESTAURANT_OWNER, CUSTOMER)
- `Address.java` - Objeto embutido (@Embeddable)

#### 5. **DTOs** (`dto/`)
- `UserCreateRequest` - Requisição de criação
- `UserUpdateRequest` - Atualização (sem senha)
- `ChangePasswordRequest` - Alteração de senha
- `LoginValidateRequest` - Validação de login
- `UserResponse` - Resposta ao cliente (sem passwordHash)
- `AddressDTO` - Transferência de endereço

#### 6. **Exception Handling** (`exception/`)
- `GlobalExceptionHandler.java` - Centraliza tratamento de erros
- Respostas padronizadas com RFC 7807 (ProblemDetail)
- Custom exceptions com semântica específica

#### 7. **Configuration** (`config/`)
- `SecurityConfig.java` - Bean de BCryptPasswordEncoder
- `OpenApiConfig.java` - Customização do Swagger UI

---

## 📊 Modelagem de Dados

### Entidade User

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
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

| Campo | Tipo | Regras | Validação |
|-------|------|--------|-----------|
| `id` | BigInt | PK, Auto-increment | - |
| `name` | String | NOT NULL, 255 chars | @NotBlank |
| `email` | String | NOT NULL, UNIQUE | @Email + regex |
| `login` | String | NOT NULL, UNIQUE | @NotBlank |
| `passwordHash` | String | NOT NULL, BCrypt | @NotBlank |
| `role` | Enum | RESTAURANT_OWNER, CUSTOMER | @NotNull |
| `address` | Object | Embeddable | @Valid |
| `lastModifiedAt` | LocalDateTime | Auto-atualizado | @UpdateTimestamp |

### Roles Disponíveis
- **RESTAURANT_OWNER** - Proprietário de restaurante
- **CUSTOMER** - Cliente/Usuário

---

## 🔌 Endpoints da API

### Base URL
```
http://localhost:8080/api/v1
```

### Autenticação

#### `POST /auth/validate`
Valida credenciais de login sem guardar sessão.

**Request:**
```json
{
  "login": "joao.silva",
  "password": "Senha@123"
}
```

**Response (200 OK):**
```json
{
  "message": "Credenciais válidas"
}
```

**Response (401 Unauthorized):**
```json
{
  "type": "https://api.fiap.com/errors/401",
  "title": "Credenciais Inválidas",
  "status": 401,
  "detail": "Login ou senha incorretos",
  "instance": "/api/v1/auth/validate",
  "errorCode": "INVALID_CREDENTIALS"
}
```

---

### Usuários

#### `POST /users`
Cria um novo usuário com validação de email duplicado.

**Request:**
```json
{
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "password": "Senha@123",
  "role": "CUSTOMER",
  "address": {
    "street": "Rua A",
    "number": "123",
    "complement": "Apto 45",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  }
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "role": "CUSTOMER",
  "address": {
    "street": "Rua A",
    "number": "123",
    "complement": "Apto 45",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  }
}
```

**Response (409 Conflict):** Email duplicado
```json
{
  "type": "https://api.fiap.com/errors/409",
  "title": "E-mail Duplicado",
  "status": 409,
  "detail": "Email 'joao@example.com' já está registrado",
  "instance": "/api/v1/users",
  "errorCode": "DUPLICATE_EMAIL"
}
```

---

#### `GET /users/{id}`
Obtém informações de um usuário específico.

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "role": "CUSTOMER",
  "address": {...}
}
```

**Response (404 Not Found):**
```json
{
  "type": "https://api.fiap.com/errors/404",
  "title": "Usuário Não Encontrado",
  "status": 404,
  "detail": "Usuário com ID 999 não encontrado",
  "errorCode": "USER_NOT_FOUND"
}
```

---

#### `GET /users?name=João`
Busca usuários por nome (case-insensitive).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "role": "CUSTOMER"
  }
]
```

---

#### `PUT /users/{id}`
Atualiza dados do usuário (exceto senha).

**Request:**
```json
{
  "name": "João Silva Santos",
  "email": "joao.novo@example.com",
  "address": {...}
}
```

**Response (200 OK):** Usuário atualizado
**Response (409 Conflict):** Email duplicado em outro usuário

---

#### `PATCH /users/{id}/password`
Altera a senha do usuário.

**Request:**
```json
{
  "currentPassword": "Senha@123",
  "newPassword": "NovaSenha@456",
  "confirmPassword": "NovaSenha@456"
}
```

**Response (204 No Content):** Sucesso sem body
**Response (401 Unauthorized):** Senha atual incorreta
**Response (400 Bad Request):** Senhas não conferem

---

#### `DELETE /users/{id}`
Remove um usuário do banco de dados.

**Response (204 No Content):** Usuário deletado
**Response (404 Not Found):** Usuário não existe

---

## 🔐 Tratamento de Erros (RFC 7807)

Todos os erros seguem o padrão **ProblemDetail (RFC 7807)** com estrutura padronizada:

```json
{
  "type": "https://api.fiap.com/errors/{status}",
  "title": "Descrição do Erro",
  "status": 400,
  "detail": "Detalhes específicos do erro",
  "instance": "/api/v1/endpoint",
  "timestamp": "2026-01-03T15:30:00Z",
  "traceId": "uuid-único-para-rastreamento",
  "severity": "error",
  "errorCode": "UNIQUE_ERROR_CODE"
}
```

### Códigos de Status HTTP

| Status | Situação | Exemplo |
|--------|----------|---------|
| **200** | Sucesso | GET, POST válido |
| **201** | Recurso criado | POST /users |
| **204** | Sem conteúdo | DELETE, PATCH sucesso |
| **400** | Requisição inválida | Validação falha |
| **401** | Não autorizado | Login/senha errado |
| **404** | Não encontrado | GET ID inexistente |
| **409** | Conflito | Email duplicado |
| **500** | Erro interno | Exceção não tratada |

### Exception Handlers

- `UserNotFoundException` → 404 NOT_FOUND
- `DuplicateEmailException` → 409 CONFLICT
- `InvalidLoginException` → 401 UNAUTHORIZED
- `MethodArgumentNotValidException` → 400 BAD_REQUEST com `validationErrors` por campo
- `IllegalArgumentException` → 400 BAD_REQUEST
- `Exception` (genérica) → 500 INTERNAL_SERVER_ERROR

---

## 🔐 Segurança de Senhas

### Armazenamento
- Senhas armazenadas com **BCrypt** (hash não reversível)
- Força configurada em 12 iterations (14 ops/segundo - balanceado)
- Nunca armazenar senhas em texto plano

### Em Endpoints
- Senha NUNCA é retornada em responses
- Validação de login usa `BCryptPasswordEncoder.matches()`
- Endpoint separado para alteração de senha (PATCH)
- Requer senha atual para validação

### Exemplo de BCrypt em Java
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);  // 12 rounds
}

// Encoding na criação
String encodedPassword = passwordEncoder.encode(rawPassword);

// Validação no login
boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
```

---

## 📚 Documentação OpenAPI (Swagger)

### Acesso
```
http://localhost:8080/swagger-ui/index.html
```

### Specs JSON
```
http://localhost:8080/v3/api-docs
```

Todos os endpoints estão documentados com:
- Descrição clara
- Exemplos de request/response
- Validações esperadas
- Códigos de status possíveis

---

## 🚀 Como Executar o Projeto

### Opção 1: Com Docker Compose (Recomendado)

#### Pré-requisitos
- Docker (v20+)
- Docker Compose (v2+)

#### Passo a Passo

1. **Clone o repositório e navegue até o diretório**
   ```bash
   cd c:\Users\iluiz\Documents\MyProjects\fiap-tech-challenge-1
   ```

2. **Build e start dos containers**
   ```bash
   docker-compose up --build
   ```
   
   Isso irá:
   - Criar imagem Docker da aplicação
   - Iniciar PostgreSQL na porta 5432
   - Iniciar aplicação na porta 8080
   - Criar volumes para persistência

3. **Verificar status**
   ```bash
   curl http://localhost:8080/actuator/health
   ```
   
   Resposta esperada:
   ```json
   {
     "status": "UP",
     "components": {
       "db": {
         "status": "UP",
         "details": {
           "database": "PostgreSQL"
         }
       }
     }
   }
   ```

4. **Parar containers**
   ```bash
   docker-compose down
   ```

---

### Opção 2: Execução Local com Maven

#### Pré-requisitos
- JDK 17+
- Maven 3.9+
- PostgreSQL 15 rodando localmente

#### Passo a Passo

1. **Configurar variáveis de ambiente** (aplicação esperará em localhost:5432)
   
   Criar arquivo `.env` ou editar `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/user_management
   spring.datasource.username=postgres
   spring.datasource.password=sua_senha
   ```

2. **Compilar projeto**
   ```bash
   mvn clean compile
   ```

3. **Executar testes**
   ```bash
   mvn test
   ```

4. **Rodar aplicação**
   ```bash
   mvn spring-boot:run
   ```
   
   Saída esperada:
   ```
   Tomcat started on port(s): 8080 (http)
   Started UserManagementApplication
   ```

5. **Acessar Swagger**
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

---

### Opção 3: Build Docker Manual

```bash
# Build imagem
docker build -t fiap-user-management:latest .

# Rodar container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/user_management \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  fiap-user-management:latest
```

---

## 📮 Importar Postman Collection

### Passo a Passo

1. **Abrir Postman**
   - Clique em "Import"
   - Selecione "Import File" ou "Import from Link"

2. **Importar arquivo `postman_collection.json`**
   - Localize `postman_collection.json` no repositório
   - Clique em "Import"

3. **Configurar variáveis**
   - Selecione collection "FIAP User Management API"
   - Abra a aba "Variables"
   - Defina `baseUrl = http://localhost:8080`

4. **Testar endpoints**
   - Expanda folders na collection
   - Clique em qualquer request
   - Pressione "Send"

### Cenários pré-configurados

✅ Cadastro válido
✅ Cadastro com email duplicado
✅ Atualização de dados
✅ Troca de senha
✅ Busca por nome
✅ Validação de login (sucesso)
✅ Validação de login (erro)
✅ Health check

---

## 📋 Estrutura de Diretórios

```
fiap-tech-challenge-1/
├── src/
│   ├── main/
│   │   ├── java/com/fiap/challenge/
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java
│   │   │   │   └── AuthController.java
│   │   │   ├── service/
│   │   │   │   └── UserService.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Address.java
│   │   │   │   └── UserRole.java
│   │   │   ├── dto/
│   │   │   │   ├── UserCreateRequest.java
│   │   │   │   ├── UserUpdateRequest.java
│   │   │   │   ├── ChangePasswordRequest.java
│   │   │   │   ├── LoginValidateRequest.java
│   │   │   │   ├── UserResponse.java
│   │   │   │   └── AddressDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── UserNotFoundException.java
│   │   │   │   ├── DuplicateEmailException.java
│   │   │   │   ├── InvalidLoginException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   └── UserManagementApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/fiap/challenge/
│       │   ├── controller/
│       │   ├── service/
│       │   └── IntegrationTest.java
│       └── resources/
│           └── application-test.properties
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── postman_collection.json
├── README.md
└── RELATORIO.md
```

---

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=UserServiceTest

# Com cobertura
mvn test jacoco:report
```

### Tipos de Testes Inclusos
- ✅ Testes unitários de serviço
- ✅ Testes de integração com repository
- ✅ Testes E2E de controller
- ✅ Validação de entidades

---

## 📖 Documentação Adicional

- [RELATORIO.md](RELATORIO.md) - Relatório técnico para avaliação
- [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) - Guia de testes manuais
- [IMPROVEMENTS_DETAILED.md](IMPROVEMENTS_DETAILED.md) - Detalhes técnicos das melhorias

---

## 🤝 Contribuindo

Para adicionar features ou corrigir bugs:

1. Criar branch: `git checkout -b feature/sua-feature`
2. Fazer commits: `git commit -m "feat: descrição"`
3. Push: `git push origin feature/sua-feature`
4. Abrir Pull Request

---

## 📝 Licença

Este projeto é licenciado sob MIT License - veja [LICENSE](LICENSE) para detalhes.

---

## 👨‍💻 Desenvolvedor

**FIAP Tech Challenge - Fase 1**
Desenvolvido em 2026

---

## ❓ FAQ

### P: Como resetar o banco de dados?
**R:** Com Docker Compose:
```bash
docker-compose down -v
docker-compose up --build
```

### P: Posso usar outro banco que não seja PostgreSQL?
**R:** Sim, altere em `application.properties` e `docker-compose.yml`, mas testes foram feitos com PostgreSQL.

### P: Como adicionar um novo endpoint?
**R:** 
1. Criar método em `UserService`
2. Criar endpoint em `UserController` com `@PostMapping`, etc
3. Documentar com `@Operation` e `@ApiResponse`

### P: Está seguro guardar senha em BCrypt?
**R:** Sim! BCrypt é hash não reversível com salt aleatório. Senhas de exemplo em Postman são apenas para teste.

---

**Desenvolvido com ❤️ em Spring Boot 3 e Java 17**
