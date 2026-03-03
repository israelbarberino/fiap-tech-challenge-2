# Arquitetura da Aplicação - FIAP Tech Challenge Phase 2

## 📋 Visão Geral

A aplicação segue o padrão de **Clean Architecture** com separação clara de responsabilidades em camadas:

```
┌─────────────────────────────────────────────────────────┐
│                   APRESENTAÇÃO (Controllers)              │
│     UserTypeController, RestaurantController, ...        │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                  APLICAÇÃO (Services)                    │
│    UserTypeService, RestaurantService, MenuItemService  │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                   DOMÍNIO (Entities)                     │
│    UserType, Restaurant, MenuItem, User, Address        │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│              INFRAESTRUTURA (Repositories)              │
│  UserTypeRepository, RestaurantRepository, ...          │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                 BANCO DE DADOS (PostgreSQL)             │
│              JPA/Hibernate ORM Mapping                  │
└─────────────────────────────────────────────────────────┘
```

## 🏗️ Estrutura de Diretórios

```
src/
├── main/
│   ├── java/com/fiap/challenge/
│   │   ├── config/                 # Configurações da aplicação
│   │   ├── controller/             # Controllers REST
│   │   │   ├── UserController
│   │   │   ├── UserTypeController
│   │   │   ├── RestaurantController
│   │   │   └── MenuItemController
│   │   ├── service/                # Lógica de negócio
│   │   │   ├── UserService
│   │   │   ├── UserTypeService
│   │   │   ├── RestaurantService
│   │   │   └── MenuItemService
│   │   ├── repository/             # Data access (JPA)
│   │   │   ├── UserRepository
│   │   │   ├── UserTypeRepository
│   │   │   ├── RestaurantRepository
│   │   │   └── MenuItemRepository
│   │   ├── entity/                 # Modelos de domínio
│   │   │   ├── User
│   │   │   ├── UserType
│   │   │   ├── Restaurant
│   │   │   ├── MenuItem
│   │   │   ├── Address
│   │   │   └── UserRole
│   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── UserTypeRequestDTO / ResponseDTO
│   │   │   ├── RestaurantRequestDTO / ResponseDTO
│   │   │   └── MenuItemRequestDTO / ResponseDTO
│   │   ├── exception/              # Tratamento de exceções
│   │   │   └── GlobalExceptionHandler
│   │   └── UserManagementApplication.java
│   └── resources/
│       ├── application.properties  # Configurações
│       └── init.sql               # Script SQL inicial
└── test/
    └── java/com/fiap/challenge/
        ├── service/               # Testes unitários dos serviços
        │   ├── UserTypeServiceTest
        │   ├── RestaurantServiceTest
        │   └── MenuItemServiceTest
        └── integration/           # Testes de integração
            ├── UserTypeIntegrationTest
            ├── RestaurantIntegrationTest
            └── MenuItemIntegrationTest
```

## 📦 Componentes Principais

### 1. **Entities (Domain Layer)**

#### UserType
Representa um tipo de usuário no sistema (ex: "Dono de Restaurante", "Cliente")

```java
@Entity
public class UserType {
    @Id private Long id;
    @Column(unique = true) private String name;
    private String description;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime lastModifiedAt;
}
```

#### Restaurant
Representa um restaurante cadastrado na plataforma

```java
@Entity
public class Restaurant {
    @Id private Long id;
    @Column(unique = true) private String name;
    private String cuisineType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    @Embedded private Address address;
    @ManyToOne private User owner;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime lastModifiedAt;
}
```

#### MenuItem
Representa um item do cardápio de um restaurante

```java
@Entity
public class MenuItem {
    @Id private Long id;
    private String name;
    private String description;
    @Column(precision = 10, scale = 2) private BigDecimal price;
    private Boolean availableOnPremises;
    private String photoPath;
    @ManyToOne private Restaurant restaurant;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime lastModifiedAt;
}
```

### 2. **DTOs (Transfer Objects)**

**Request DTOs** - Para receber dados da API
- `UserTypeRequestDTO`
- `RestaurantRequestDTO`
- `MenuItemRequestDTO`

**Response DTOs** - Para enviar dados da API
- `UserTypeResponseDTO`
- `RestaurantResponseDTO`
- `MenuItemResponseDTO`

**Benefícios:**
- Separação entre dados internos (Entity) e dados públicos (DTO)
- Validação centralizada
- Controle sobre dados expostos

### 3. **Services (Business Logic)**

#### UserTypeService
```java
- create(UserTypeRequestDTO): UserTypeResponseDTO
- getById(Long id): UserTypeResponseDTO
- getByName(String name): UserTypeResponseDTO
- getAll(): List<UserTypeResponseDTO>
- update(Long id, UserTypeRequestDTO): UserTypeResponseDTO
- delete(Long id): void
```

#### RestaurantService
```java
- create(RestaurantRequestDTO): RestaurantResponseDTO
- getById(Long id): RestaurantResponseDTO
- getByName(String name): RestaurantResponseDTO
- getAll(): List<RestaurantResponseDTO>
- getByCuisineType(String cuisineType): List<RestaurantResponseDTO>
- update(Long id, RestaurantRequestDTO): RestaurantResponseDTO
- delete(Long id): void
```

#### MenuItemService
```java
- create(MenuItemRequestDTO): MenuItemResponseDTO
- getById(Long id): MenuItemResponseDTO
- getAll(): List<MenuItemResponseDTO>
- getByRestaurantId(Long restaurantId): List<MenuItemResponseDTO>
- update(Long id, MenuItemRequestDTO): MenuItemResponseDTO
- delete(Long id): void
```

### 4. **Controllers (REST Endpoints)**

Cada controller fornece endpoints CRUD para seu recurso:

- **UserTypeController**: `/api/v1/user-types`
- **RestaurantController**: `/api/v1/restaurants`
- **MenuItemController**: `/api/v1/menu-items`

**Recursos:**
- Documentação OpenAPI/Swagger automática
- Validação com `@Valid` e `@NotBlank`
- Tratamento de exceções global
- Respostas HTTP apropriadas (201, 200, 204, 400, 404)

### 5. **Repositories (Data Access)**

Interfaces JPA para acesso aos dados:

```java
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByName(String name);
}

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    Optional<Restaurant> findByOwner(User owner);
    List<Restaurant> findByCuisineType(String cuisineType);
}

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    Optional<MenuItem> findByRestaurantAndName(Restaurant restaurant, String name);
}
```

## 🔄 Fluxo de Requisição

1. **Controller** recebe a requisição HTTP
2. **Controller** converte para DTO e chama o **Service**
3. **Service** valida lógica de negócio
4. **Service** utiliza **Repository** para acesso ao BD
5. **Repository** executa query via JPA
6. **Service** converte Entity para DTO
7. **Controller** retorna Response ao cliente

## 🔐 Padrões de Projeto

### Clean Code
- Nomes descritivos e significativos
- Métodos pequenos e focados
- Separação de responsabilidades
- DRY (Don't Repeat Yourself)

### SOLID
- **Single Responsibility**: Cada classe tem uma única razão para mudar
- **Open/Closed**: Classes abertas para extensão, fechadas para modificação
- **Liskov Substitution**: Implementações do JpaRepository são substituíveis
- **Interface Segregation**: DTOs específicos para cada operação
- **Dependency Inversion**: Injeção de dependência via constructor

### Design Patterns
- **Repository Pattern**: Abstração do acesso a dados
- **DTO Pattern**: Transferência de dados entre camadas
- **Service Layer Pattern**: Lógica de negócio centralizada
- **Dependency Injection**: Injeção via Spring Container

## 🗄️ Banco de Dados

### Diagrama ER

```
┌─────────────────┐          ┌──────────────────┐
│  USER_TYPES     │          │      USERS       │
├─────────────────┤          ├──────────────────┤
│ id (PK)         │          │ id (PK)          │
│ name (UNIQUE)   │          │ email (UNIQUE)   │
│ description     │          │ login (UNIQUE)   │
│ created_at      │          │ name             │
│ last_modified_at│          │ password_hash    │
└─────────────────┘          │ role             │
                              │ address_*        │
                              │ created_at       │
                              │ last_modified_at │
                              └────────┬─────────┘
                                       │ 1
                                       │ (owner)
                                       │ *
┌──────────────────────────────────────┴──────┐
│          RESTAURANTS                        │
├─────────────────────────────────────────────┤
│ id (PK)                                     │
│ name (UNIQUE)                               │
│ cuisine_type                                │
│ opening_time                                │
│ closing_time                                │
│ address_street                              │
│ address_number                              │
│ address_complement                          │
│ address_city                                │
│ address_state                               │
│ address_zip_code                            │
│ owner_id (FK, UNIQUE)                       │
│ created_at                                  │
│ last_modified_at                            │
└─────────────────┬──────────────────────────┘
                  │ 1
                  │ (restaurant)
                  │ *
┌─────────────────▼────────────────────────────┐
│         MENU_ITEMS                          │
├──────────────────────────────────────────────┤
│ id (PK)                                      │
│ name                                         │
│ description                                  │
│ price (DECIMAL 10,2)                         │
│ available_on_premises                        │
│ photo_path                                   │
│ restaurant_id (FK)                           │
│ created_at                                   │
│ last_modified_at                             │
│ UQ: (restaurant_id, name)                    │
└──────────────────────────────────────────────┘
```

### Scripts de Criação

```sql
CREATE TABLE user_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP NOT NULL
);

CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL,
    cuisine_type VARCHAR(100) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    address_street VARCHAR(255) NOT NULL,
    address_number VARCHAR(50) NOT NULL,
    address_complement VARCHAR(255),
    address_city VARCHAR(100) NOT NULL,
    address_state VARCHAR(2) NOT NULL,
    address_zip_code VARCHAR(10) NOT NULL,
    owner_id BIGINT UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (owner_id)
);

CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    available_on_premises BOOLEAN NOT NULL,
    photo_path VARCHAR(500),
    restaurant_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
    UNIQUE (restaurant_id, name)
);
```

## 🧪 Estratégia de Testes

### Testes Unitários (com Mockito)
- Isolam a unidade de código (método)
- Mockam dependências externas
- Executam rapidamente
- Cobertura de 80%+

**Suites:**
- `UserTypeServiceTest` - 10 testes
- `RestaurantServiceTest` - 9 testes
- `MenuItemServiceTest` - 10 testes

### Testes de Integração (com MockMvc)
- Testam a integração entre camadas
- Usam banco de dados de teste
- Simulam requisições HTTP reais
- Validam responses completas

**Suites:**
- `UserTypeIntegrationTest` - 7 testes
- `RestaurantIntegrationTest` - 7 testes
- `MenuItemIntegrationTest` - 9 testes

### Execução

```bash
# Todos os testes
mvn test

# Testes com cobertura
mvn verify

# Relatório JaCoCo
target/site/jacoco/index.html
```

## 🔄 Ciclo de Vida da Aplicação

```
1. Spring Boot inicia
2. Carrega application.properties
3. Cria DataSource (PostgreSQL)
4. Inicializa JPA/Hibernate
5. Mapeia Entities para tabelas
6. Executa init.sql (se configurado)
7. Registra Repositories
8. Injeta dependências nos Services
9. Injeta dependências nos Controllers
10. Inicia servidor Tomcat em :8080
11. Expõe endpoints REST
12. Habilita Swagger em /swagger-ui.html
```

## 📊 Métricas de Qualidade

- **Cobertura de Código**: 80%+
- **Complexidade Ciclomática**: Baixa (métodos simples)
- **Duplicação de Código**: < 5%
- **Vulnerabilidades Conhecidas**: 0 (CVE check)
- **Padrões de Código**: Seguem convenções Spring Boot

## 🚀 Próximos Passos (Fase 3)

- [ ] Autenticação JWT
- [ ] Autorização role-based (RBAC)
- [ ] Paginação nos GETs
- [ ] Busca avançada com filtros
- [ ] Upload de imagens (fotos dos pratos)
- [ ] Avaliações e comentários
- [ ] Integração com sistemas de pagamento
- [ ] Notificações em tempo real (WebSocket)

---

**Versão:** 2.0 (Phase 2)  
**Data:** Março 2026  
**Stack:** Java 17 + Spring Boot 3.2.1 + PostgreSQL 15
