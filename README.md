# FIAP Tech Challenge - User Management API

API REST para gerenciamento de usuários com Spring Boot 3.2.1, Java 17 e PostgreSQL 15.

**📖 [Documentação Online](https://seu-usuario.github.io/fiap-tech-challenge-1)** | **🔗 [Setup GitHub Pages](GITHUB_PAGES_SETUP.md)** | **📊 [Diagramas Mermaid](DIAGRAMS.md)**

## 🚀 Como Executar

### Docker (Recomendado)

```bash
docker-compose up --build
```

Acesse:
- API: http://localhost:8080/api/v1
- Swagger: http://localhost:8080/swagger-ui.html

### Localmente

```bash
# 1. Configure PostgreSQL
createdb user_management

# 2. Compile e execute
mvn clean install
mvn spring-boot:run
```

## 📡 Endpoints

**Base:** `http://localhost:8080/api/v1`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/validate` | Valida credenciais |
| POST | `/users` | Cria usuário |
| GET | `/users/{id}` | Busca por ID |
| GET | `/users?name=` | Busca por nome |
| PUT | `/users/{id}` | Atualiza usuário |
| PATCH | `/users/{id}/password` | Altera senha |
| DELETE | `/users/{id}` | Remove usuário |

## 🧪 Testes

```bash
mvn test              # Executar testes
mvn verify            # Com cobertura JaCoCo
```

Cobertura: 95.21% (53 testes: 17 unitários + 36 integração)

## 🛠️ Stack

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- PostgreSQL 15
- BCrypt para senhas
- JUnit 5 + Mockito
- Docker

## 📚 Exemplos

### Criar Usuário

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "login": "joao.silva",
    "password": "Senha@123",
    "role": "CUSTOMER"
  }'
```

### Validar Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "Senha@123"
  }'
```

---

**Licença:** MIT
