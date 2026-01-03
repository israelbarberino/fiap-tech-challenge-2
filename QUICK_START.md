# Quick Start Guide

Guia rápido para começar com o projeto User Management API.

## ⚡ 5 Minutos para Começar

### Pré-requisitos
- Docker e Docker Compose instalados
- OU Java 17 + Maven 3.6 + PostgreSQL 13

### Opção 1: Com Docker (Recomendado)

```bash
# 1. Clone/acesse o repositório
cd fiap-tech-challenge-1

# 2. Inicie os containers
docker-compose up --build

# 3. Aguarde inicialização (2-3 minutos)
# Veja a mensagem: "Started UserManagementApplication"

# 4. Acesse
# Swagger UI: http://localhost:8080/swagger-ui.html
# Health: http://localhost:8080/actuator/health
```

### Opção 2: Localmente

```bash
# 1. Certifique-se que PostgreSQL está rodando
psql -U postgres -c "CREATE DATABASE user_management;"

# 2. Compile e execute
mvn clean install
mvn spring-boot:run

# 3. Acesse
# http://localhost:8080/swagger-ui.html
```

## 📝 Primeiro Exemplo - Criar um Usuário

### Via cURL

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
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
