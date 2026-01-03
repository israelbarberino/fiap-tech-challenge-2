# Como Contribuir

## 🎯 Diretrizes

Este é um projeto de **Phase 1 do Tech Challenge da FIAP** e segue padrões específicos de qualidade.

### Padrões de Código

✅ **Java 17 LTS**  
✅ **Spring Boot 3.2.1**  
✅ **Clean Architecture**  
✅ **SOLID Principles**  
✅ **Testes (JUnit 5 + Mockito)**  
✅ **Cobertura > 90%**  

### Antes de Fazer Commit

```bash
# 1. Rodar testes
mvn test

# 2. Verificar cobertura
mvn verify

# 3. Verificar formatação
# (Sem formatter automático neste projeto, mas mantenha consistência)
```

## 🔧 Como Adicionar um Novo Endpoint

### 1. Adicione a lógica na Service

```java
// UserService.java
@Transactional
public void novoMetodo(Long id, Request request) {
    // Validações
    // Lógica
    // Persistência
}
```

### 2. Crie a resposta (DTO se necessário)

```java
public record NovaResponse(...) {}
```

### 3. Adicione o endpoint no Controller

```java
@PostMapping("/novo")
@Operation(summary = "...", description = "...")
@ApiResponse(responseCode = "200", ...)
public ResponseEntity<NovaResponse> novoEndpoint(
        @Valid @RequestBody NovaRequest request) {
    return ResponseEntity.ok(userService.novoMetodo(request));
}
```

### 4. Adicione testes

```java
@Test
@DisplayName("Deve processar novo endpoint com sucesso")
void testNovoEndpoint() throws Exception {
    // Arrange
    // Act
    // Assert
}
```

### 5. Documente no Postman

Adicione requisição em `postman_collection.json`

## 🧪 Padrão de Testes

### Unit Tests (Service)

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @Test
    @DisplayName("Deve processar com sucesso")
    void testSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        
        // Act
        UserResponse result = service.getUserById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("João", result.getName());
        verify(repository, times(1)).findById(1L);
    }
}
```

### Integration Tests (Controller)

```java
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    @DisplayName("Deve retornar 200 com sucesso")
    void testSuccess() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);
        
        mockMvc.perform(get("/api/v1/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }
}
```

### E2E Tests (Integration)

```java
@IntegrationTest
@AutoConfigureMockMvc
class UserManagementE2ETest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("Fluxo completo: criar → atualizar → deletar")
    void testCompleteLifecycle() throws Exception {
        // 1. Create
        // 2. Read
        // 3. Update
        // 4. Delete
        // 5. Verify deleted
    }
}
```

## 📝 Padrão de Commits

```
[TIPO]: Descrição breve

Descrição mais longa (opcional)
- Mudança 1
- Mudança 2

TIPOS: feat, fix, docs, test, refactor, chore
```

**Exemplos:**
```
feat: Adicionar endpoint de listagem de usuários

- Novo endpoint GET /api/v1/users com paginação
- Testes unitários e integração
- Documentação no Swagger

fix: Corrigir validação de email duplicado

test: Aumentar cobertura de UserService para 95%

docs: Atualizar ARCHITECTURE.md com novo padrão
```

## 🔍 Code Review Checklist

- [ ] Código segue SOLID Principles?
- [ ] Há testes unitários?
- [ ] Há testes de integração?
- [ ] Cobertura > 90%?
- [ ] Tratamento de erros (exceptions)?
- [ ] Validações (3 camadas)?
- [ ] DTOs usados corretamente?
- [ ] Swagger documentado?
- [ ] Variáveis de ambiente configuradas?
- [ ] sem hardcoding?

## 📚 Documentação

Sempre atualize:
- [ ] Comentários no código
- [ ] README.md (se breaking changes)
- [ ] ARCHITECTURE.md (se novo padrão)
- [ ] QUICK_START.md (se novo requirement)
- [ ] Postman Collection (novos endpoints)
- [ ] Swagger annotations

## 🚫 Não Fazer

❌ Remover testes  
❌ Reduzir cobertura abaixo de 90%  
❌ Usar Spring Security Authentication (não é requisito Phase 1)  
❌ Adicionar JWT tokens (não é requisito Phase 1)  
❌ Mudar estrutura de pastas sem razão  
❌ Criar endpoints fora de `/api/v1`  
❌ Hardcoding de valores  
❌ Ignorar validações  
❌ Fazer commits sem testes passando  

## ✅ Fazer

✅ Escrever testes primeiro (TDD opcional)  
✅ Usar DTOs para request/response  
✅ Validar em 3 camadas  
✅ Usar ProblemDetail para erros  
✅ Documentar com Swagger  
✅ Adicionar ao Postman Collection  
✅ Fazer commits atômicos  
✅ Revisar próprio código antes de PR  

## 🔄 Workflow

1. **Fork** ou **branch** do `main`
2. **Crie feature branch**: `git checkout -b feature/sua-feature`
3. **Desenvolva** com testes
4. **Commit**: `git commit -m "[TIPO]: Descrição"`
5. **Push**: `git push origin feature/sua-feature`
6. **Pull Request** com descrição
7. **Code Review**
8. **Merge** após aprovação

## 📞 Dúvidas?

Veja:
- [ARCHITECTURE.md](ARCHITECTURE.md) - Arquitetura e padrões
- [README.md](README.md) - Overview rápido
- [QUICK_START.md](QUICK_START.md) - Setup
- [INDEX.md](INDEX.md) - Índice de documentação

---

**Obrigado por contribuir!** 🙏
