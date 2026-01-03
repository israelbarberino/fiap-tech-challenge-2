---
layout: default
title: FIAP Tech Challenge - Índice
---

# 📚 Índice de Documentação para GitHub Pages

## 🏠 Página Inicial
- **[Landing Page](docs/index.html)** - Página inicial com diagramas e informações principais

## 📖 Documentação Principal

### Começar Rápido
- **[Quick Start](QUICK_START.md)** - Setup em 5 minutos com Docker
- **[README](README.md)** - Overview do projeto e endpoints

### Arquitetura & Design
- **[Arquitetura Técnica](ARCHITECTURE.md)** - Deep dive em Clean Architecture e SOLID
- **[Diagramas Mermaid](DIAGRAMS.md)** - 12 diagramas visuais (Clean Architecture, fluxos, ER, etc.)
- **[Resumo do Projeto](PROJECT_SUMMARY.md)** - Métricas, checklist e estrutura completa

### Mais Informações
- **[Índice de Navegação](INDEX.md)** - Navegação por persona (Dev, QA, DevOps, etc.)
- **[Como Contribuir](CONTRIBUTING.md)** - Padrões de código e diretrizes
- **[Relatório de Conclusão](COMPLETION_REPORT.md)** - Tudo que foi entregue

## 🌐 Publicação GitHub Pages

### Setup & Deployment
- **[Setup Guide](GITHUB_PAGES_SETUP.md)** - Passo-a-passo para ativar Pages (10 passos)
- **[Publication Summary](GITHUB_PAGES_PUBLICATION.md)** - Resumo completo do que foi criado
- **[Pages Summary](PAGES_SUMMARY.md)** - Guia rápido com 3 passos

### Dashboards
- **[Status Dashboard](docs/pages-status.html)** - Dashboard visual de status

## 🔌 API & Testes

### Testar Localmente
- **[Postman Collection](postman_collection.json)** - 18 requisições HTTP completas
- **Swagger UI** (rodando) - `http://localhost:8080/swagger-ui.html`

## 📊 Diagramas Disponíveis

Todos em **[DIAGRAMS.md](DIAGRAMS.md)**:

1. 🏗️ **Clean Architecture** - 5 camadas com componentes
2. 🔄 **Request-Response Cycle** - Fluxo completo de requisição
3. 🗄️ **Entity-Relationship** - Modelo de banco de dados
4. 🔐 **Security & Validation** - Fluxo de validação 3 camadas
5. 📊 **Test Coverage** - Distribuição de testes (pie chart)
6. 📈 **Coverage by Layer** - Cobertura por camada
7. 🚀 **Deployment Flow** - CI/CD pipeline com GitHub Actions
8. 🔌 **API Endpoints** - Mapa de todos os 7 endpoints
9. 📦 **Dependencies** - Stack tecnológico (Java, Spring, PostgreSQL, etc.)
10. 🔍 **Exception Handling** - Estratégia de tratamento de erros
11. 👥 **Class Diagram** - Pacotes e classes Java
12. 🧬 **Domain Model** - Estrutura de domínio (User, Address, UserRole)

## 🛠️ Stack Tecnológico

- **Java 17 LTS** - Linguagem base
- **Spring Boot 3.2.1** - Framework principal
- **PostgreSQL 15** - Banco de dados relacional
- **Docker** - Containerização
- **JUnit 5 + Mockito** - Testes automáticos (95.21% coverage)
- **SpringDoc OpenAPI** - Documentação Swagger

## ✅ Requisitos Implementados

- ✅ 7 endpoints REST completos
- ✅ 2 tipos de usuário (RESTAURANT_OWNER, CUSTOMER)
- ✅ CRUD completo
- ✅ Autenticação (login + senha)
- ✅ Troca de senha (PATCH)
- ✅ Atualização (sem senha)
- ✅ Busca por nome (case-insensitive)
- ✅ Validação em 3 camadas
- ✅ Clean Architecture (5 camadas)
- ✅ SOLID Principles
- ✅ 53 testes (95.21% cobertura)
- ✅ Docker + docker-compose
- ✅ Swagger/OpenAPI
- ✅ RFC 7807 (ProblemDetail) para erros
- ✅ 6+ arquivos de documentação
- ✅ Postman Collection (18 requisições)

## 🚀 Como Começar

### Com Docker (Recomendado)
```bash
docker-compose up --build
# Acesse: http://localhost:8080/swagger-ui.html
```

### Localmente
```bash
createdb user_management
mvn clean install
mvn spring-boot:run
# Acesse: http://localhost:8081/swagger-ui.html
```

### Testes
```bash
mvn test          # Testes
mvn verify        # Com cobertura JaCoCo
mvn jacoco:report # Relatório HTML
```

## 📱 Acesso Rápido

| Recurso | Descrição |
|---------|-----------|
| **Landing Page** | Visão geral com diagramas |
| **Quick Start** | Setup em 5 minutos |
| **Arquitetura** | Deep dive técnico |
| **Diagramas** | 12 visualizações Mermaid |
| **API Docs** | Swagger (rodando) |
| **Postman** | 18 testes HTTP |

## 🎯 Por Persona

### 👨‍💼 Gerente/Avaliador
- Leia: [Resumo Projeto](PROJECT_SUMMARY.md) + [Landing Page](docs/index.html)
- Veja: [Status Dashboard](docs/pages-status.html)
- Tempo: 5-10 minutos

### 👨‍💻 Desenvolvedor Backend
- Leia: [Arquitetura](ARCHITECTURE.md) + [Diagramas](DIAGRAMS.md)
- Setup: [Quick Start](QUICK_START.md)
- Contribua: [Guidelines](CONTRIBUTING.md)
- Tempo: 20-30 minutos

### 🧪 QA/Tester
- Use: [Postman Collection](postman_collection.json)
- Leia: [Resumo Projeto](PROJECT_SUMMARY.md) - seção Testes
- Veja: [API Docs Swagger](http://localhost:8080/swagger-ui.html)
- Tempo: 15-20 minutos

### 🐳 DevOps
- Setup: [Quick Start](QUICK_START.md)
- Docker: [Dockerfile](Dockerfile) + [docker-compose.yml](docker-compose.yml)
- CI/CD: [Setup Guide](GITHUB_PAGES_SETUP.md)
- Tempo: 10-15 minutos

## 📞 Suporte & Links

### Documentação Oficial
- [GitHub Pages Docs](https://docs.github.com/en/pages)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Mermaid Docs](https://mermaid.js.org)
- [Docker Docs](https://docs.docker.com/)

### Dentro deste Projeto
- **Problemas?** Veja [Troubleshooting](QUICK_START.md#troubleshooting)
- **Setup?** Veja [Setup Guide](GITHUB_PAGES_SETUP.md)
- **Diagramas?** Veja [Diagrams](DIAGRAMS.md)
- **Código?** Veja [Contributing](CONTRIBUTING.md)

---

**Criado**: 3 de janeiro de 2026  
**Projeto**: FIAP Tech Challenge - Phase 1  
**Status**: ✅ Production Ready  
**Cobertura**: 95.21% (53 testes)
