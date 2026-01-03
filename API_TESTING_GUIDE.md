# API Testing Guide

Examples and patterns for testing the User Management API.

---

## 📌 Prerequisites

- cURL or Postman installed
- Application running on `http://localhost:8080`
- PostgreSQL running (Docker or local)

---

## 🔌 Request Examples

### 1. Create User

**Request:**
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
      "street": "Rua das Flores",
      "number": "123",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01234-567"
    }
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "role": "CUSTOMER",
  "createdAt": "2026-01-03T16:42:00Z",
  "lastModifiedAt": "2026-01-03T16:42:00Z"
}
```

---

### 2. Get User by ID

```bash
curl -X GET http://localhost:8080/api/v1/users/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "role": "CUSTOMER",
  "address": {...},
  "createdAt": "2026-01-03T16:42:00Z",
  "lastModifiedAt": "2026-01-03T16:42:00Z"
}
```

---

### 3. Search by Name

```bash
curl "http://localhost:8080/api/v1/users?name=João"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "role": "CUSTOMER"
  }
]
```

---

### 4. Update User

```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Updated",
    "email": "joao.new@example.com",
    "role": "RESTAURANT_OWNER",
    "address": {
      "street": "Rua Atualizada",
      "number": "456",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "02234-567"
    }
  }'
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva Updated",
  "email": "joao.new@example.com",
  "role": "RESTAURANT_OWNER",
  "lastModifiedAt": "2026-01-03T16:45:00Z"
}
```

---

### 5. Change Password

```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "Senha@123",
    "newPassword": "NovaSenha@456",
    "confirmPassword": "NovaSenha@456"
  }'
```

**Response (204 No Content):**
```
(empty body)
```

---

### 6. Delete User

```bash
curl -X DELETE http://localhost:8080/api/v1/users/1
```

**Response (204 No Content):**
```
(empty body)
```

---

### 7. Validate Credentials

```bash
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "Senha@123"
  }'
```

**Success (200 OK):**
```json
{
  "message": "Credenciais válidas"
}
```

**Failure (401):**
```json
{
  "type": "https://api.example.com/errors/401",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid credentials"
}
```

---

## ❌ Error Scenarios

### Invalid Email Format

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "invalid-email",  # Missing @domain
    "login": "test.user",
    "password": "Weak123",  # Missing special char
    "role": "CUSTOMER"
  }'
```

**Response (400 Bad Request):**
```json
{
  "type": "https://api.example.com/errors/400",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed",
  "errorCode": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    },
    {
      "field": "password",
      "message": "Password must contain special character"
    }
  ]
}
```

---

### Duplicate Email

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Duplicate",
    "email": "joao@example.com",  # Already exists
    "login": "duplicate.user",
    "password": "NewPass@123",
    "role": "CUSTOMER"
  }'
```

**Response (409 Conflict):**
```json
{
  "type": "https://api.example.com/errors/409",
  "title": "Conflict - Duplicate Email",
  "status": 409,
  "detail": "Email 'joao@example.com' is already registered",
  "errorCode": "DUPLICATE_EMAIL"
}
```

---

### User Not Found

```bash
curl -X GET http://localhost:8080/api/v1/users/999
```

**Response (404 Not Found):**
```json
{
  "type": "https://api.example.com/errors/404",
  "title": "Not Found",
  "status": 404,
  "detail": "User with ID 999 not found",
  "errorCode": "USER_NOT_FOUND"
}
```

---

## 📮 Using Postman

1. Import `postman_collection.json` into Postman
2. Set environment variable: `base_url = http://localhost:8080`
3. Run requests in sequence:
   - Create User
   - Get User
   - Update User
   - Validate Credentials
   - Change Password
   - Delete User

Each request includes example data and validation checks.

---

## 🧪 Testing Checklist

- [ ] Create user with valid data → 201
- [ ] Create user with invalid email → 400
- [ ] Create user with duplicate email → 409
- [ ] Get existing user → 200
- [ ] Get non-existent user → 404
- [ ] Search by name → 200 with results
- [ ] Update user → 200
- [ ] Change password → 204
- [ ] Delete user → 204
- [ ] Validate correct credentials → 200
- [ ] Validate incorrect credentials → 401

---

## 📚 Additional Resources

- [README.md](README.md) – Overview
- [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) – Complete technical report
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) – Common issues

### 6. Validar Login e Senha

```bash
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "novaSenha456"
  }'
```

**Response (200 OK - Credenciais Válidas):**
```json
{
  "message": "Credenciais válidas"
}
```

**Response (401 Unauthorized - Credenciais Inválidas):**
```json
{
  "type": "about:blank",
  "title": "Não Autorizado",
  "status": 401,
  "detail": "Senha incorreta",
  "instance": "/api/v1/auth/validate",
  "timestamp": "2026-01-03T10:40:00Z"
}
```

### 7. Deletar Usuário

```bash
curl -X DELETE http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json"
```

**Response (204 No Content):**
```
(sem corpo de resposta)
```

## ❌ Exemplos de Erros

### Erro 400 - Validação Inválida

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "invalid-email",
    "login": "",
    "password": "",
    "role": "USER",
    "address": {}
  }'
```

**Response (400 Bad Request):**
```json
{
  "type": "about:blank",
  "title": "Erro de Validação",
  "status": 400,
  "detail": "Um ou mais campos foram validados incorretamente",
  "instance": "/api/v1/users",
  "timestamp": "2026-01-03T10:45:00Z",
  "errors": {
    "name": "Nome não pode estar vazio",
    "email": "Email deve ser válido",
    "login": "Login não pode estar vazio",
    "password": "Senha não pode estar vazia"
  }
}
```

### Erro 409 - Email Duplicado

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "joao@example.com",
    "login": "outro.joao",
    "password": "senha123",
    "role": "USER",
    "address": {...}
  }'
```

**Response (409 Conflict):**
```json
{
  "type": "about:blank",
  "title": "Conflito - Recurso Duplicado",
  "status": 409,
  "detail": "Email já está registrado: joao@example.com",
  "instance": "/api/v1/users",
  "timestamp": "2026-01-03T10:50:00Z"
}
```

### Erro 404 - Usuário Não Encontrado

```bash
curl -X GET http://localhost:8080/api/v1/users/999 \
  -H "Content-Type: application/json"
```

**Response (404 Not Found):**
```json
{
  "type": "about:blank",
  "title": "Usuário Não Encontrado",
  "status": 404,
  "detail": "Usuário não encontrado com ID: 999",
  "instance": "/api/v1/users/999",
  "timestamp": "2026-01-03T10:55:00Z"
}
```

## 🧪 Usando Postman

1. **Importar Collection**
   - Abrir Postman
   - File → Import
   - Inserir a URL da API: `http://localhost:8080/v3/api-docs`
   - Postman criará uma collection automaticamente

2. **Configurar Variáveis de Ambiente**
   - Criar novo Environment
   - Adicionar variável: `BASE_URL` = `http://localhost:8080`
   - Usar `{{BASE_URL}}/api/v1/users` nas requisições

## 🧬 Testes Automatizados

```bash
# Executar testes unitários
mvn test

# Executar com cobertura
mvn test jacoco:report

# Executar teste específico
mvn test -Dtest=UserServiceTest
mvn test -Dtest=UserControllerTest
```

## 📊 Monitoramento

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Spec
```
http://localhost:8080/v3/api-docs
```
