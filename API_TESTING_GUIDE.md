# API Testing Guide

Este guia fornece exemplos de como testar os endpoints da API User Management.

## 📌 Pré-requisitos

- cURL ou Postman instalado
- Aplicação rodando em `http://localhost:8080`
- PostgreSQL rodando com dados iniciais (veja `init.sql`)

## 🔌 Exemplos de Requisições

### 1. Criar Usuário

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "password": "senha123",
    "role": "CUSTOMER",
    "address": {
      "street": "Rua das Flores",
      "number": "123",
      "complement": "Apto 456",
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
  "role": "USER",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "complement": "Apto 456",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  },
  "createdAt": "2026-01-03T10:30:00",
  "lastModifiedAt": "2026-01-03T10:30:00"
}
```

### 2. Obter Usuário por ID

```bash
curl -X GET http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json"
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "login": "joao.silva",
  "role": "USER",
  "address": {...},
  "createdAt": "2026-01-03T10:30:00",
  "lastModifiedAt": "2026-01-03T10:30:00"
}
```

### 3. Buscar Usuários por Nome

```bash
curl -X GET "http://localhost:8080/api/v1/users?name=João" \
  -H "Content-Type: application/json"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "role": "USER",
    "address": {...},
    "createdAt": "2026-01-03T10:30:00",
    "lastModifiedAt": "2026-01-03T10:30:00"
  }
]
```

### 4. Atualizar Usuário

```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Updated",
    "email": "joao.updated@example.com",
    "role": "RESTAURANT_OWNER",
    "address": {
      "street": "Rua Atualizada",
      "number": "456",
      "complement": "Sala 789",
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
  "email": "joao.updated@example.com",
  "login": "joao.silva",
  "role": "RESTAURANT_OWNER",
  "address": {...},
  "createdAt": "2026-01-03T10:30:00",
  "lastModifiedAt": "2026-01-03T10:35:00"
}
```

### 5. Alterar Senha

```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "senha123",
    "newPassword": "novaSenha456",
    "confirmPassword": "novaSenha456"
  }'
```

**Response (204 No Content):**
```
(sem corpo de resposta)
```

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
