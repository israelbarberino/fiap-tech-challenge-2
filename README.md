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

### Autenticação e Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/validate` | Valida credenciais |
| POST | `/users` | Cria usuário |
| GET | `/users/{id}` | Busca usuário por ID |
| GET | `/users?name=` | Busca usuário por nome |
| PUT | `/users/{id}` | Atualiza usuário |
| PATCH | `/users/{id}/password` | Altera senha |
| DELETE | `/users/{id}` | Remove usuário |

### Tipos de Usuário

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/user-types` | Cria tipo de usuário |
| GET | `/user-types` | Lista todos os tipos |
| GET | `/user-types/{id}` | Busca tipo por ID |
| GET | `/user-types/name/{name}` | Busca tipo por nome |
| PUT | `/user-types/{id}` | Atualiza tipo de usuário |
| DELETE | `/user-types/{id}` | Remove tipo de usuário |

### Restaurantes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/restaurants` | Cria restaurante |
| GET | `/restaurants` | Lista todos os restaurantes |
| GET | `/restaurants/{id}` | Busca restaurante por ID |
| GET | `/restaurants/name/{name}` | Busca restaurante por nome |
| GET | `/restaurants/cuisine/{cuisineType}` | Lista por tipo de cozinha |
| PUT | `/restaurants/{id}` | Atualiza restaurante |
| DELETE | `/restaurants/{id}` | Remove restaurante |

### Itens de Cardápio

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/menu-items` | Cria item de cardápio |
| GET | `/menu-items` | Lista todos os itens |
| GET | `/menu-items/{id}` | Busca item por ID |
| GET | `/menu-items/restaurant/{restaurantId}` | Lista itens por restaurante |
| PUT | `/menu-items/{id}` | Atualiza item de cardápio |
| DELETE | `/menu-items/{id}` | Remove item de cardápio |

## 🧪 Testes

```bash
mvn test              # Executar testes
mvn verify            # Com cobertura JaCoCo
```

**Cobertura de Testes:**
- 9 testes unitários (UserTypeService, RestaurantService, MenuItemService)
- 9 testes de integração (UserType, Restaurant, MenuItem endpoints)
- **Meta de Cobertura:** 80%+

**Suites de Teste:**
- `UserTypeServiceTest` - Testes unitários do serviço de tipos de usuário
- `RestaurantServiceTest` - Testes unitários do serviço de restaurantes
- `MenuItemServiceTest` - Testes unitários do serviço de itens de cardápio
- `UserTypeIntegrationTest` - Testes E2E dos endpoints de tipos de usuário
- `RestaurantIntegrationTest` - Testes E2E dos endpoints de restaurantes
- `MenuItemIntegrationTest` - Testes E2E dos endpoints de itens de cardápio

## 🛠️ Stack

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- PostgreSQL 15
- BCrypt para senhas
- JUnit 5 + Mockito
- Docker

## 📚 Exemplos

### Criar Tipo de Usuário

```bash
curl -X POST http://localhost:8080/api/v1/user-types \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dono de Restaurante",
    "description": "Usuário que é proprietário de um restaurante"
  }'
```

### Criar Restaurante

```bash
curl -X POST http://localhost:8080/api/v1/restaurants \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Trattoria Italia",
    "cuisineType": "Italiana",
    "openingTime": "11:00:00",
    "closingTime": "22:00:00",
    "address": {
      "street": "Avenida Paulista",
      "number": "1000",
      "complement": "Apt 101",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01311-100"
    },
    "ownerId": 1
  }'
```

### Criar Item de Cardápio

```bash
curl -X POST http://localhost:8080/api/v1/menu-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pasta Carbonara",
    "description": "Massa fresca com molho de ovos, bacon e queijo",
    "price": 45.50,
    "availableOnPremises": true,
    "photoPath": "/images/pasta-carbonara.jpg",
    "restaurantId": 1
  }'
```

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

## 📦 Collection Postman

Uma collection Postman completa está disponível em `postman_phase2_collection.json`. 
Importe no Postman para testar todos os endpoints com exemplos pré-configurados.

---

**Licença:** MIT


URL LATEX: https://www.overleaf.com/project/6959a35c227815c078bca22d