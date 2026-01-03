# Quick Start Guide

Get started with the User Management API in 5 minutes.

---

## ⚡ Prerequisites

- Docker & Docker Compose, **OR**
- Java 17 + Maven 3.6 + PostgreSQL 15

---

## 🚀 Option 1: Docker (Recommended)

```bash
# Clone or navigate to project
cd fiap-tech-challenge-1

# Start containers
docker-compose up --build

# Wait for startup (2-3 minutes)
# Look for: "Started UserManagementApplication"

# Access API
curl http://localhost:8080/actuator/health

# Open Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## 🖥️ Option 2: Local Development

```bash
# 1. Create database
createdb user_management

# 2. Build project
mvn clean install

# 3. Run application
mvn spring-boot:run

# 4. Access Swagger
open http://localhost:8080/swagger-ui.html
```

---

## 📝 First API Call – Create a User

### Using cURL

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

## 🔑 Validate Credentials

```bash
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "Senha@123"
  }'
```

**Response (200 OK):**
```json
{
  "message": "Credenciais válidas"
}
```

**Invalid (401):**
```json
{
  "type": "https://api.example.com/errors/401",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid credentials"
}
```

---

## 🔍 Search User by Name

```bash
curl http://localhost:8080/api/v1/users?name=João
```

---

## 📋 Available Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/users` | Create user |
| GET | `/api/v1/users/{id}` | Get by ID |
| GET | `/api/v1/users?name=` | Search by name |
| PUT | `/api/v1/users/{id}` | Update user |
| PATCH | `/api/v1/users/{id}/password` | Change password |
| DELETE | `/api/v1/users/{id}` | Delete user |
| POST | `/api/v1/auth/validate` | Validate login |

---

## 🧪 Run Tests

```bash
# All tests
mvn clean verify

# Unit tests only
mvn test

# With coverage report
mvn jacoco:report
open target/site/jacoco/index.html
```

---

## 🛑 Troubleshooting

**Container won't start?**
```bash
docker-compose down
docker-compose up --build
```

**Port 8080 already in use?**
```bash
# Change in docker-compose.yml or application.properties
lsof -i :8080  # See what's using it
```

**Database connection error?**
```bash
# Check PostgreSQL is running
docker ps
docker logs fiap-tech-challenge-1_postgres_1
```

---

## 📚 Next Steps

- Read [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) for detailed examples
- Check [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) for complete documentation
- See [BEST_PRACTICES.md](BEST_PRACTICES.md) for architectural patterns
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "password": "Senha123!",
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

### Via Swagger UI

1. Abra http://localhost:8080/swagger-ui.html
2. Expanda **Users** → **POST /api/v1/users**
3. Clique em **Try it out**
4. Preencha os dados
5. Clique em **Execute**

## 🧪 Cenários de Teste Completos

### Cenário 1: Criar e Buscar Usuário

```bash
# Passo 1: Criar usuário
USER_ID=$(curl -s -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Santos",
    "email": "maria@example.com",
    "login": "maria.santos",
    "password": "SenhaSegura123!",
    "role": "CUSTOMER",
    "address": {
      "street": "Avenida Paulista",
      "number": "1000",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01311-100"
    }
  }' | jq -r '.id')

echo "Usuário criado com ID: $USER_ID"

# Passo 2: Buscar usuário
curl -X GET http://localhost:8080/api/v1/users/$USER_ID \
  -H "Content-Type: application/json"
```

### Cenário 2: Validar Login

```bash
# Login válido
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "maria.santos",
    "password": "SenhaSegura123!"
  }'

# Resultado esperado (200 OK):
# {"message": "Credenciais válidas"}

# Login inválido (senha errada)
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "maria.santos",
    "password": "senhaErrada"
  }'

# Resultado esperado (401 Unauthorized):
# {"title": "Não Autorizado", "status": 401, ...}
```

### Cenário 3: Alterar Dados

```bash
# Passo 1: Atualizar usuário
curl -X PUT http://localhost:8080/api/v1/users/$USER_ID \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Santos Silva",
    "email": "maria.silva@example.com",
    "role": "RESTAURANT_OWNER",
    "address": {
      "street": "Avenida Brasil",
      "number": "2000",
      "city": "Rio de Janeiro",
      "state": "RJ",
      "zipCode": "20000-000"
    }
  }'

# Passo 2: Trocar senha
curl -X PATCH http://localhost:8080/api/v1/users/$USER_ID/password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "SenhaSegura123!",
    "newPassword": "NovaSenha456!",
    "confirmPassword": "NovaSenha456!"
  }'

# Passo 3: Verificar se a nova senha funciona
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "maria.santos",
    "password": "NovaSenha456!"
  }'
```

### Cenário 4: Buscar por Nome

```bash
# Criar múltiplos usuários
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao1@example.com",
    "login": "joao1",
    "password": "Senha123!",
    "role": "CUSTOMER",
    "address": {...}
  }'

curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Santos",
    "email": "joao2@example.com",
    "login": "joao2",
    "password": "Senha123!",
    "role": "CUSTOMER",
    "address": {...}
  }'

# Buscar todos os usuários com "João" no nome
curl -X GET "http://localhost:8080/api/v1/users?name=João" \
  -H "Content-Type: application/json"

# Resultado: Array com 2 usuários encontrados
```

### Cenário 5: Testar Erros

```bash
# Erro 400: Email inválido
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste",
    "email": "email-invalido",
    "login": "teste",
    "password": "Senha123!",
    "role": "USER",
    "address": {...}
  }'

# Erro 409: Email duplicado
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "joao@example.com",  # Email já existe!
    "login": "outro",
    "password": "Senha123!",
    "role": "USER",
    "address": {...}
  }'

# Erro 404: Usuário não encontrado
curl -X GET http://localhost:8080/api/v1/users/9999
```

## 🔍 Verificar Status da Aplicação

```bash
# Health check
curl http://localhost:8080/actuator/health

# Informações da aplicação
curl http://localhost:8080/actuator/info

# Métricas
curl http://localhost:8080/actuator/metrics
```

## 📊 Usar Postman

### 1. Importar Collection

```bash
# Via URL
POST http://localhost:8080/v3/api-docs
```

Em Postman:
1. File → Import
2. Selecionar URL
3. Colar: `http://localhost:8080/v3/api-docs`
4. Importar

### 2. Criar Variáveis de Ambiente

```json
{
  "base_url": "http://localhost:8080",
  "user_id": "1",
  "email": "teste@example.com"
}
```

### 3. Usar em Requisições

```
POST {{base_url}}/api/v1/users
GET {{base_url}}/api/v1/users/{{user_id}}
```

## 🧬 Usar httpie (Alternativa ao cURL)

```bash
# Instalar
pip install httpie

# Criar usuário
http POST http://localhost:8080/api/v1/users \
  name="João Silva" \
  email="joao@example.com" \
  login="joao" \
  password="Senha123!" \
  role="USER" \
  address:='{"street":"Rua","number":"123","city":"São Paulo","state":"SP","zipCode":"01234-567"}'

# Buscar
http GET http://localhost:8080/api/v1/users/1

# Validar login
http POST http://localhost:8080/api/v1/auth/validate \
  login="joao" \
  password="Senha123!"
```

## 📱 Usar VSCode REST Client

Instale extensão "REST Client" e crie arquivo `test.http`:

```http
@baseUrl = http://localhost:8080
@email = test@example.com

### Criar usuário
POST {{baseUrl}}/api/v1/users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "{{email}}",
  "login": "joao",
  "password": "Senha123!",
  "role": "USER",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  }
}

### Buscar usuário
GET {{baseUrl}}/api/v1/users/1

### Validar login
POST {{baseUrl}}/api/v1/auth/validate
Content-Type: application/json

{
  "login": "joao",
  "password": "Senha123!"
}
```

Clique em **Send Request** acima de cada requisição.

## 🚀 Scripts Úteis

### Criar múltiplos usuários (Bash)

```bash
#!/bin/bash

for i in {1..5}; do
  curl -s -X POST http://localhost:8080/api/v1/users \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"Usuário $i\",
      \"email\": \"user$i@example.com\",
      \"login\": \"user$i\",
      \"password\": \"Senha123!\",
      \"role\": \"USER\",
      \"address\": {
        \"street\": \"Rua $i\",
        \"number\": \"$((100 + i))\",
        \"city\": \"São Paulo\",
        \"state\": \"SP\",
        \"zipCode\": \"0123$i-567\"
      }
    }" | jq '.id'
done
```

### Teste de carga (Apache Bench)

```bash
# Instalar: apt-get install apache2-utils

ab -n 100 -c 10 http://localhost:8080/actuator/health
```

## 📚 Próximos Passos

1. **Ler documentação:**
   - [README.md](README.md) - Documentação completa
   - [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) - Exemplos detalhados

2. **Explorar código:**
   - Abrir projeto em IDE
   - Estudar estrutura de packages
   - Entender fluxo: Controller → Service → Repository

3. **Customizar:**
   - Adicionar novos endpoints
   - Adicionar validações
   - Adicionar testes

4. **Deploy:**
   - Configurar banco de dados real
   - Definir variáveis de ambiente
   - Usar CI/CD (GitHub Actions, etc)

## 🆘 Problemas?

Veja [TROUBLESHOOTING.md](TROUBLESHOOTING.md) para soluções.

---

**Aproveite! 🚀**
