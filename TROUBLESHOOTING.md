# Troubleshooting & FAQ

## 🔧 Problemas Comuns

### PostgreSQL

#### Erro: Connection refused

```
Exception in thread "main" java.sql.SQLException: 
Connection to localhost:5432 refused
```

**Solução:**
```bash
# Verificar se PostgreSQL está rodando
# Linux/Mac
sudo systemctl status postgresql
docker ps | grep postgres

# Windows
# Verificar em Services ou usar docker
docker-compose up postgres -d
```

#### Erro: Database "user_management" does not exist

**Solução:**
```bash
# Criar banco manualmente
psql -U postgres -c "CREATE DATABASE user_management;"

# Ou deixar o Hibernate criar (hibernte.ddl-auto=create)
spring.jpa.hibernate.ddl-auto=create
```

#### Erro: FATAL: role "postgres" does not exist

**Solução:**
```bash
# Mudar senha padrão do PostgreSQL
psql -U postgres
ALTER USER postgres WITH PASSWORD 'postgres';
```

### Maven

#### Erro: [ERROR] Failed to execute goal on project

**Solução:**
```bash
# Limpar cache Maven
mvn clean
rm -rf ~/.m2/repository

# Reinstalar dependências
mvn dependency:resolve
mvn clean install
```

#### Erro: [ERROR] COMPILATION ERROR: 

**Solução:**
```bash
# Verificar versão Java
java -version
# Deve ser Java 17+

# Verificar pom.xml
mvn compiler:compile -X
```

### Spring Boot

#### Erro: Cannot find @Controller or @RestController in classpath

**Solução:**
```xml
<!-- Certificar que spring-boot-starter-web está em pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

#### Erro: Port 8080 is already in use

**Solução:**
```bash
# Encontrar processo usando porta 8080
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou mudar porta em application.properties
server.port=8090
```

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

## 🔗 Links Úteis

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
