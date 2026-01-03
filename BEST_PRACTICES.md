# Best Practices Implemented

Este documento descreve as melhores práticas implementadas neste projeto.

## 🏗️ Arquitetura

### Clean Architecture
```
Presentation Layer (Controllers)
        ↓
Business Logic Layer (Services)
        ↓
Data Access Layer (Repositories)
        ↓
Database Layer (Entities)
```

**Benefícios:**
- Separação clara de responsabilidades
- Fácil manutenção e testes
- Independência entre camadas
- Reutilização de código

### Dependency Injection
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    
    // Constructor Injection (preferred)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Por que:** Constructor Injection é melhor que Field Injection porque:
- Facilita testes (não precisa reflection)
- Deixa dependências explícitas
- Permite imutabilidade com `final`
- Detecta circular dependencies

## 🔐 Segurança

### Password Hashing
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12); // strength = 12
}
```

**Standard Industry:**
- BCrypt com strength ≥ 10
- Strength 12 = ~14 hashes/segundo
- Adequado para proteger contra força bruta

### Never Expose Sensitive Data
```java
public class UserResponse {
    // ❌ NÃO fazer isto
    // private String passwordHash;
    
    // ✅ Fazer isto - sem senha
    private String id;
    private String name;
    private String email;
}
```

### Input Validation
```java
public class UserCreateRequest {
    @NotBlank(message = "Nome não pode estar vazio")
    private String name;
    
    @Email(message = "Email deve ser válido")
    private String email;
}
```

**Layers of Validation:**
1. Bean Validation (@NotBlank, @Email, etc)
2. Service Layer (regras de negócio)
3. Database Constraints (unique, not null)

## 📊 Database

### Unique Constraints
```java
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email", name = "uk_user_email"),
    @UniqueConstraint(columnNames = "login", name = "uk_user_login")
})
public class User {
    // ...
}
```

### Automatic Timestamps
```java
@CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(nullable = false)
private LocalDateTime lastModifiedAt;
```

### Embedded Objects
```java
@Embedded
private Address address;
```

**Benefícios:**
- Agrupa dados relacionados
- Reutiliza validações
- Simplifica a entidade

## 🧪 Testing

### Unit Tests (Service Layer)
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
}
```

### Integration Tests (Controller Layer)
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
```

### Test Coverage
- Service: 100% das funcionalidades
- Controller: Todos os endpoints e códigos de erro
- Exception Handling: Todos os cenários de erro

## 📚 API Design

### RESTful Standards
```
POST   /api/v1/users              → Criar (201)
GET    /api/v1/users/{id}         → Obter (200)
PUT    /api/v1/users/{id}         → Atualizar (200)
PATCH  /api/v1/users/{id}/password → Atualizar parcial (204)
DELETE /api/v1/users/{id}         → Deletar (204)
GET    /api/v1/users?name=        → Buscar (200)
```

### Versioning
```
/api/v1/  → Versão 1
/api/v2/  → Versão 2 (no futuro)
```

### Proper HTTP Status Codes
- **201 Created:** Recurso criado com sucesso
- **204 No Content:** Operação bem-sucedida sem resposta
- **400 Bad Request:** Validação falhou
- **401 Unauthorized:** Credenciais inválidas
- **404 Not Found:** Recurso não encontrado
- **409 Conflict:** Violação de constraint (email duplicado)

### Error Response (RFC 7807)
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

## 📖 Documentation

### JavaDoc em Métodos Públicos
```java
/**
 * Busca um usuário pelo ID
 * 
 * @param id o ID do usuário
 * @return UserResponse com os dados do usuário
 * @throws UserNotFoundException se o usuário não for encontrado
 */
public UserResponse getUserById(Long id) {
    // ...
}
```

### Swagger/OpenAPI
```java
@Operation(
    summary = "Criar novo usuário",
    description = "Cria um novo usuário no sistema",
    responses = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já registrado")
    }
)
public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    // ...
}
```

## 🔄 Transaction Management

### @Transactional
```java
@Service
@Transactional
public class UserService {
    // Todos os métodos rodam em transação
    // Rollback automático em exceção
}
```

**Benefícios:**
- Atomicidade: Tudo ou nada
- Consistência: Estado válido
- Isolamento: Operações paralelas seguras
- Durabilidade: Dados persistidos

## 🧠 Code Quality

### SOLID Principles

#### Single Responsibility
```java
// ✅ Bom: Cada classe tem um propósito
public class UserService {     // Lógica de usuário
    // ...
}

public class PasswordValidator { // Validação de senha
    // ...
}

// ❌ Ruim: Responsabilidades misturadas
public class UserManager {
    // Cria, deleta, valida, salva em cache...
}
```

#### Open/Closed
```java
// ✅ Aberto para extensão, fechado para modificação
public interface UserRepository extends JpaRepository<User, Long> {
    // Fácil estender com novos métodos
}
```

#### Dependency Inversion
```java
// ✅ Depender de abstrações, não de implementações
public class UserService {
    private final UserRepository repository; // Interface
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### DRY (Don't Repeat Yourself)
```java
// ✅ Código reutilizável
private User findUserOrThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("..."));
}

// ❌ Repetição
public UserResponse getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("..."))
        .toResponse();
}
```

### KISS (Keep It Simple, Stupid)
```java
// ✅ Simples e claro
public boolean validateLogin(LoginValidateRequest request) {
    User user = userRepository.findByLogin(request.getLogin())
        .orElseThrow(() -> new InvalidLoginException("..."));
    
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
        throw new InvalidLoginException("...");
    }
    
    return true;
}

// ❌ Complexo desnecessariamente
public boolean validateLogin(LoginValidateRequest request) {
    // 10 linhas de lógica complexa
}
```

## ⚡ Performance

### Query Optimization
```java
// ✅ Busca por campos únicos
Optional<User> findByEmail(String email);

// ✅ Busca com paginação
List<User> findByNameContainingIgnoreCase(String name);

// ❌ Evitar buscar tudo
// @Query("SELECT u FROM User u")
// List<User> findAll(); // Heavy!
```

### Connection Pooling
```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

## 🔍 Logging

### Structured Logging
```java
logger.info("User created successfully", Map.of(
    "userId", userId,
    "email", email,
    "timestamp", LocalDateTime.now()
));

logger.error("User not found", Map.of(
    "id", id,
    "stacktrace", exception.getStackTrace()
));
```

### Appropriate Log Levels
- **DEBUG:** Detalhes técnicos, valores de variáveis
- **INFO:** Eventos importantes do negócio
- **WARN:** Situações incomuns mas esperadas
- **ERROR:** Erros que precisam atenção

## 🐳 Containerization

### Docker Best Practices
```dockerfile
# ✅ Multi-stage build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
# Build stage

FROM eclipse-temurin:17-jre-alpine
# Runtime stage (menor, sem Maven)

# ✅ Usuário não-root
RUN addgroup -g 1000 appuser && adduser -D -u 1000 -G appuser appuser
USER appuser

# ✅ Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD ["wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
```

## 🚀 Deployment

### Environment Variables
```bash
# .env (NÃO commitar)
DATABASE_URL=jdbc:postgresql://host:5432/db
DB_USERNAME=user
DB_PASSWORD=password
SPRING_PROFILES_ACTIVE=production
```

### Configuration by Environment
```properties
# application-dev.properties (desenvolvimento)
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# application-prod.properties (produção)
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

## 📋 Code Review Checklist

- [ ] Código segue convenções de nomenclatura
- [ ] Métodos têm responsabilidade única
- [ ] Erros são tratados adequadamente
- [ ] Testes cobrem o código novo
- [ ] Documentação está atualizada
- [ ] Sem código duplicado
- [ ] Performance foi considerada
- [ ] Segurança foi verificada
- [ ] Logs são apropriados

---

## 🎯 Conclusão

Este projeto implementa as melhores práticas de desenvolvimento de software:
- ✅ Arquitetura limpa e bem estruturada
- ✅ Segurança em camadas
- ✅ Testes abrangentes
- ✅ Documentação completa
- ✅ Código legível e manutenível
- ✅ DevOps ready (Docker, CI/CD)

Seguindo estas práticas, o código é mais:
- **Seguro** - Menos vulnerabilidades
- **Escalável** - Cresce com demanda
- **Manutenível** - Fácil entender e modificar
- **Testável** - Alto grau de cobertura
- **Performático** - Otimizado para produção
