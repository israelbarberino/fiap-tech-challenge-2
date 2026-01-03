# Troubleshooting & FAQ

Common issues and solutions.

---

## 🐘 PostgreSQL Issues

### Connection Refused

**Problem:**
```
java.sql.SQLException: Connection to localhost:5432 refused
```

**Solution:**
```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Start via Docker Compose
docker-compose up postgres -d

# Or verify local PostgreSQL
sudo systemctl status postgresql
```

---

### Database Does Not Exist

**Problem:**
```
ERROR: database "user_management" does not exist
```

**Solution:**
```bash
# Create database
createdb user_management

# Or let Hibernate create it
# In application.properties:
spring.jpa.hibernate.ddl-auto=create
```

---

### Invalid Credentials

**Problem:**
```
FATAL: role "postgres" does not exist
```

**Solution:**
```bash
# Reset PostgreSQL password
psql -U postgres
ALTER USER postgres WITH PASSWORD 'postgres';
```

---

## 🏗️ Maven Issues

### Dependency Resolution Fails

**Problem:**
```
[ERROR] Failed to execute goal on project
```

**Solution:**
```bash
# Clean cache
mvn clean
rm -rf ~/.m2/repository

# Reinstall
mvn dependency:resolve
mvn clean install
```

---

### Compilation Error

**Problem:**
```
[ERROR] COMPILATION ERROR
```

**Solution:**
```bash
# Check Java version
java -version
# Must be Java 17+

# Rebuild
mvn clean compile
```

---

## 🚀 Spring Boot Issues

### Port Already In Use

**Problem:**
```
Port 8080 is already in use
```

**Solution:**
```bash
# Find process using port
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8090
```

---

### Application Won't Start

**Problem:**
```
Failed to start embedded Tomcat
```

**Solution:**
```bash
# Check logs
docker-compose logs app

# Verify all dependencies
mvn dependency:tree

# Test locally
mvn spring-boot:run
```

---

## 🐳 Docker Issues

### Container Exit Immediately

**Problem:**
```
Container exited with code 1
```

**Solution:**
```bash
# View logs
docker-compose logs app

# Rebuild
docker-compose down
docker-compose up --build

# Check docker-compose.yml syntax
docker-compose config
```

---

### Build Takes Too Long

**Problem:**
```
Docker building for 10+ minutes
```

**Solution:**
```bash
# Use cached layers
docker-compose build --no-cache app

# Check Dockerfile
# Ensure dependencies cache layer is optimized
```

---

## 🧪 Test Issues

### Tests Fail Randomly

**Problem:**
```
Test passed locally, failed in CI
```

**Solution:**
```bash
# Run all tests sequentially
mvn test -DparallelTestClasses=false

# Increase timeout
mvn test -DargLine="-Dtimeout=10000"
```

---

### Coverage Not Reaching 90%

**Problem:**
```
Coverage: 89.5% (below threshold)
```

**Solution:**
```bash
# Generate report
mvn clean verify
mvn jacoco:report
open target/site/jacoco/index.html

# Identify uncovered lines
# Add tests for missing branches
```

---

## 🔐 Security & Validation Issues

### Password Validation Fails

**Problem:**
```
"Password must contain uppercase, digit, special character"
```

**Valid Example:**
```
Senha@123  ✅ (uppercase + digit + special)
senha123   ❌ (no uppercase or special)
Senha123!  ✅
```

---

### Email Validation Fails

**Problem:**
```
"Invalid email format"
```

**Valid Examples:**
```
user@example.com        ✅
user+tag@example.co.uk  ✅
invalid@.com            ❌
noatsign.com            ❌
```

---

### Duplicate Email Error

**Problem:**
```
"Email 'joao@example.com' is already registered"
```

**Solution:**
```bash
# Use unique email for each test
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "unique-email-'$(date +%s)'@example.com",
    ...
  }'

# Or delete previous user
curl -X DELETE http://localhost:8080/api/v1/users/1
```

---

## 🌐 API Issues

### CORS Errors

**Problem:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution:**
- Not applicable in Phase 1 (backend-only)
- CORS will be configured in Phase 2 when frontend is added

---

### 401 Unauthorized on Valid Credentials

**Problem:**
```
Status: 401 "Invalid credentials"
```

**Solution:**
```bash
# Verify password is correct
curl -X POST http://localhost:8080/api/v1/auth/validate \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "Senha@123"
  }'

# Note: Password is case-sensitive
# Must include uppercase, digit, special char if created with validation
```

---

### 404 Not Found

**Problem:**
```
"User with ID 999 not found"
```

**Solution:**
```bash
# List all users
curl "http://localhost:8080/api/v1/users?name="

# Get valid user ID first
# Then use it in subsequent requests
```

---

## 📊 Health Check

**Verify Application Status:**
```bash
curl http://localhost:8080/actuator/health
```

**Expected Response (200):**
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

---

## 📚 More Help

- [README.md](README.md) – Overview and setup
- [QUICK_START.md](QUICK_START.md) – 5-minute start
- [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) – API examples
- [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) – Complete documentation

#### Erro: Failed to initialize pool

```
Caused by: java.sql.SQLException: 
Cannot get a connection, pool error
```

**Solução:**
1. Verificar credenciais do banco em `application.properties`
2. Verificar se PostgreSQL está rodando
3. Testar conexão:
```bash
psql -h localhost -U postgres -W
```

### Docker

#### Erro: Port 5432 is already allocated

**Solução:**
```bash
# Parar container anterior
docker-compose down

# Ou usar porta diferente no docker-compose.yml
ports:
  - "5433:5432"  # Host:Container
```

#### Erro: Image build failed

**Solução:**
```bash
# Limpar imagens
docker system prune -a

# Rebuild
docker-compose build --no-cache
```

#### Erro: Container exits immediately

**Solução:**
```bash
# Ver logs
docker-compose logs app

# Ou executar interativamente
docker run -it user-management-api:latest /bin/bash
```

### Swagger/API

#### Erro: 404 Swagger UI not found

```
http://localhost:8080/swagger-ui.html → 404
```

**Solução:**
1. Verificar dependency no pom.xml
2. Limpar cache e recompilar
```bash
mvn clean compile
mvn spring-boot:run
```

#### Erro: Cannot access /v3/api-docs

**Solução:**
```bash
# Verificar logs
tail -f logs/application.log

# Verificar se SpringDoc está configurado
# application.properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Testes

#### Erro: No tests found

**Solução:**
```bash
# Verificar estrutura de diretórios
# Deve estar em: src/test/java/com/fiap/challenge/

# Executar teste específico
mvn test -Dtest=UserServiceTest
```

#### Erro: Tests hang/timeout

**Solução:**
```bash
# Adicionar timeout
mvn test -DtestFailureIgnore=true -Dsurefire.timeout=120000

# Ou verificar @Mock setup incorreto
```

---

## ❓ FAQ

### P: Qual versão de Java devo usar?
**R:** Java 17 ou superior. Verificar com `java -version`

### P: Posso usar outro banco de dados?
**R:** Sim, mas precisa alterar:
- Dependency no pom.xml
- Driver JDBC em application.properties
- Dialect do Hibernate

Exemplo para MySQL:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### P: Como adicionar autenticação JWT?
**R:** Adicionar Spring Security com JWT:
```bash
# 1. Adicionar dependency
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.11.5</version>
</dependency>

# 2. Criar JwtProvider
# 3. Adicionar filter de autenticação
# 4. Implementar endpoints de login/refresh token
```

### P: Como fazer deploy em produção?
**R:** 
```bash
# 1. Build para produção
mvn clean package -DskipTests

# 2. Usar docker-compose com variáveis de ambiente
# 3. Configurar secrets em .env (não commitar)
# 4. Usar CI/CD (GitHub Actions, GitLab CI, etc)
```

### P: Como melhorar a performance?
**R:**
1. **Indexar campos** - Adicionar @Index em entidades
2. **Cache** - Adicionar Spring Cache
3. **Connection Pool** - Aumentar `maximum-pool-size` em HikariCP
4. **Queries** - Usar @Query com projeções
5. **Paginação** - Adicionar Pageable nos endpoints

### P: Como adicionar logging estruturado?
**R:**
```xml
<!-- Adicionar Logback JSON -->
<dependency>
    <groupId>ch.qos.logback.contrib</groupId>
    <artifactId>logback-json-classic</artifactId>
    <version>0.1.5</version>
</dependency>
```

### P: Como fazer integração com Kafka/RabbitMQ?
**R:**
```xml
<!-- Para Kafka -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<!-- Para RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### P: Posso usar Gradle em vez de Maven?
**R:** Sim, converta o pom.xml:
```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.1'
}

java {
    sourceCompatibility = '17'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    // ... outros
}
```

### P: Como ativar HTTPS/SSL?
**R:**
```properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.port=8443
```

### P: Como adicionar rate limiting?
**R:**
```xml
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

### P: Como configurar CORS?
**R:**
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*")
                    .allowedHeaders("*");
            }
        };
    }
}
```

### P: Como usar variáveis de ambiente?
**R:**
```properties
# application.properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/user_management}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

```bash
# Executar com variáveis
export DATABASE_URL="jdbc:postgresql://prod-server:5432/db"
mvn spring-boot:run
```

### P: Como adicionar versionamento de API?
**R:** Já está implementado! Use `/api/v1/` para versão 1.

Para adicionar nova versão:
```bash
# Copiar controller para v2
cp src/main/java/com/fiap/challenge/controller/UserController.java \
   src/main/java/com/fiap/challenge/controller/v2/UserController.java

# Atualizar @RequestMapping
@RequestMapping("/api/v2/users")
```

---

## � Segurança: Actuator Endpoints

### Endpoints Protegidos

**Mudança (Janeiro 2026):** Endpoints do Actuator foram protegidos para prevenir vazamento de informações.

**Acesso Público:**
```bash
curl http://localhost:8080/actuator/health
# Resposta: {"status":"UP"}  # Apenas status básico
```

**Acesso Autenticado (detalhes completos):**
```bash
curl -u admin:senha http://localhost:8080/actuator/health
# Requer HTTP Basic com role ACTUATOR_ADMIN
```

**Configuração:**
```bash
# Criar .env (não commitar!)
MANAGEMENT_SECURITY_USERNAME=admin
MANAGEMENT_SECURITY_PASSWORD=sua_senha_forte

# Aplicar
mvn spring-boot:run
```

**Produção:** Use secrets manager (AWS Secrets Manager, Azure Key Vault, HashiCorp Vault).

---

## �🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 📞 Suporte

Para problemas não listados:
1. Verificar logs: `tail -f logs/application.log`
2. Procurar em: [Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot)
3. GitHub Issues do projeto
4. Documentação oficial do Spring Boot
