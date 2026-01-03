# Contributing to User Management API

Obrigado por considerar contribuir para este projeto! Este documento fornece diretrizes e instruções para contribuir.

## 🤝 Código de Conduta

Este projeto adota um Código de Conduta que todos os contribuidores devem seguir. Esperamos:
- Respeito mútuo
- Comunicação construtiva
- Foco em resolver problemas, não em apontar erros
- Inclusão e diversidade

## 🐛 Como Reportar Bugs

Antes de criar um relatório de bug, verifique a lista de issues - você pode descobrir que não precisa criar um novo.

Ao reportar um bug, inclua:
- **Título claro e descritivo**
- **Descrição detalhada do comportamento observado**
- **Comportamento esperado**
- **Passos para reproduzir o problema**
- **Exemplos específicos para demonstrar**
- **Sua configuração** (SO, versão Java, etc.)

## ✨ Sugestão de Melhorias

Para sugerir melhorias:
- Use um **título claro e descritivo**
- Forneça **descrição detalhada da melhoria sugerida**
- Liste **exemplos de como funcionaria**
- Mencione **projetos similares que têm essa funcionalidade**

## 🔧 Processo de Desenvolvimento

### 1. Fork o repositório

```bash
git clone https://github.com/seu-usuario/fiap-tech-challenge-1.git
cd fiap-tech-challenge-1
```

### 2. Crie uma branch para sua feature

```bash
git checkout -b feature/sua-feature
# ou
git checkout -b fix/seu-bug-fix
```

### 3. Faça seus commits

```bash
git add .
git commit -m "Descrição clara do que foi feito"
```

Siga essas convenções de commit:
- `feat:` para novas funcionalidades
- `fix:` para correções de bugs
- `docs:` para documentação
- `style:` para formatação de código
- `refactor:` para refatoração
- `test:` para testes

**Exemplo:**
```bash
git commit -m "feat: adicionar validação de CPF no cadastro de usuários"
git commit -m "fix: corrigir bug ao atualizar endereço do usuário"
git commit -m "docs: adicionar guia de instalação"
```

### 4. Push para sua fork

```bash
git push origin feature/sua-feature
```

### 5. Crie um Pull Request

- Descreva claramente o que seu PR resolve
- Referencie issues relacionadas (ex: "Fecha #123")
- Certifique-se de que seus commits estão atualizados com a branch principal

## 📋 Checklist antes de enviar o PR

- [ ] Meu código segue o estilo do projeto
- [ ] Executei testes localmente (`mvn test`)
- [ ] Adicionei testes para novas funcionalidades
- [ ] Atualizei a documentação se necessário
- [ ] Meus commits seguem a convenção de mensagens
- [ ] Não há conflitos com a branch principal
- [ ] Removi qualquer console.log ou debug

## 🧪 Testes

Toda contribuição **deve** incluir testes:

```bash
# Executar todos os testes
mvn test

# Executar testes de um arquivo específico
mvn test -Dtest=UserServiceTest

# Executar com cobertura
mvn test jacoco:report
```

**Cobertura mínima esperada:** 70%

## 📚 Padrões de Código

### Convenções de Nomenclatura

```java
// Variáveis e métodos: camelCase
private String userName;
public void getUserById() { }

// Constantes: UPPER_SNAKE_CASE
public static final String APP_NAME = "User Management API";

// Classes: PascalCase
public class UserService { }

// Packages: lowercase com pontos
package com.fiap.challenge.service;
```

### Estrutura de Classes

```java
public class UserService {
    // 1. Constantes
    private static final String ERROR_MESSAGE = "...";
    
    // 2. Variáveis de instância
    private final UserRepository repository;
    
    // 3. Construtor
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    // 4. Métodos públicos
    public UserResponse getUser(Long id) { }
    
    // 5. Métodos privados
    private void validate(User user) { }
}
```

### Documentação

Adicione JavaDoc para métodos públicos:

```java
/**
 * Busca um usuário pelo ID
 * 
 * @param id o ID do usuário
 * @return UserResponse com os dados do usuário
 * @throws UserNotFoundException se o usuário não for encontrado
 */
public UserResponse getUserById(Long id) {
    // implementação
}
```

## 🎯 Áreas de Contribuição

Contribuições são bem-vindas em:

- ✅ **Novos testes** (sempre bem-vindo!)
- ✅ **Correção de bugs**
- ✅ **Documentação**
- ✅ **Melhorias de performance**
- ✅ **Refatoração de código**
- ✅ **Novos endpoints** (com discussão prévia)

## 📞 Dúvidas?

Abra uma issue com o label `question` ou envie um email para o mantenedor.

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença do projeto (Apache 2.0).

---

**Obrigado por contribuir! 🎉**
