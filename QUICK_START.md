# 🚀 Quick Start - 5 Minutos

## 1️⃣ Pré-requisitos

- Docker e Docker Compose instalados
- OU: Java 17 + Maven 3.6+ + PostgreSQL 15

## 2️⃣ Com Docker (Recomendado)

```bash
# Clone ou acesse o projeto
cd fiap-tech-challenge-1

# Inicie tudo
docker-compose up --build

# Aguarde 40-50 segundos até ver:
# "Started UserManagementApplication in X seconds"
```

**Acesso:**
- 🌐 Swagger: http://localhost:8080/swagger-ui.html
- 📡 API: http://localhost:8080/api/v1
- 💚 Health: http://localhost:8080/actuator/health

## 3️⃣ Localmente (sem Docker)

```bash
# 1. PostgreSQL rodando
psql -U postgres -c "CREATE DATABASE user_management;"

# 2. Compile e execute
mvn clean install
mvn spring-boot:run

# App sobe em http://localhost:8081
```

## 4️⃣ Testar (Escolha uma)

**Opção A: Swagger UI** → http://localhost:8080/swagger-ui.html  
**Opção B: cURL**

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@test.com",
    "login": "joao",
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

# Validar credenciais
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{"login": "joao", "password": "Senha@123"}'
```

**Opção C: Postman**  
Importe `postman_collection.json`

## 5️⃣ Testes

```bash
# Todos os testes
mvn test

# Com cobertura
mvn verify
```

---

## 📁 Estrutura

- `src/main/` → Código fonte
- `src/test/` → Testes (95.21% cobertura)
- `docker-compose.yml` → Infra
- `postman_collection.json` → 18 requisições
- `ARCHITECTURE.md` → Detalhes completos

---

## 🔑 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/users` | Criar usuário |
| POST | `/api/v1/auth/validate` | Validar login |
| GET | `/api/v1/users/{id}` | Buscar por ID |
| GET | `/api/v1/users?name=xxx` | Buscar por nome |
| PUT | `/api/v1/users/{id}` | Atualizar |
| PATCH | `/api/v1/users/{id}/password` | Alterar senha |
| DELETE | `/api/v1/users/{id}` | Remover |

---

## ✨ Features

✅ 7 endpoints REST  
✅ 2 tipos de usuário (RESTAURANT_OWNER, CUSTOMER)  
✅ Validação completa (3 camadas)  
✅ Erro padronizado (RFC 7807)  
✅ 53 testes (95.21% cobertura)  
✅ Swagger documentado  
✅ BCrypt para senhas  
✅ Clean Architecture  

---

## 🆘 Troubleshooting

**Porta 8080 já em uso?**
```bash
# Altere em docker-compose.yml
# ports: - "8080:8080" → "8081:8080"
```

**PostgreSQL erro de conexão?**
```bash
# Aguarde o health check passar (40+ segundos)
docker logs user_management_postgres
```

**Maven não encontrado?**
```bash
# Instale: mvn --version
# No macOS: brew install maven
```

---

Mais detalhes → [ARCHITECTURE.md](ARCHITECTURE.md)  
Documentação Completa → [README.md](README.md)
