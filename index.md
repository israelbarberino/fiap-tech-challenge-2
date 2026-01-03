# 📚 FIAP Tech Challenge Phase 1 – Complete Documentation

Welcome to the technical documentation for **FIAP Tech Challenge Phase 1: User Management API**.

This project implements a production-ready REST API for user management using **Spring Boot 3.2.1**, **Java 17**, and **PostgreSQL 15**, with **95.21% test coverage** and Clean Architecture.

---

## 🗂️ Documentation Index

### 📘 **Official Documents**

| Document | Purpose | Audience | Read Time |
|----------|---------|----------|-----------|
| [README.md](README.md) | **Project Overview** – Start here | Everyone | 5 min |
| [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) | **Academic Technical Report** – Comprehensive analysis | Evaluators, Architects | 30 min |
| [QUICK_START.md](QUICK_START.md) | **5-Minute Setup** – Get running fast | Developers | 5 min |
| [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md) | **HTTP Examples** – Test all endpoints | API Consumers, QA | 10 min |
| [BEST_PRACTICES.md](BEST_PRACTICES.md) | **Engineering Patterns** – How we built it | Developers | 15 min |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | **Common Issues & Solutions** – Support | Everyone | Variable |
| [CONTRIBUTING.md](CONTRIBUTING.md) | **Contribution Guide** – How to contribute | Contributors | 10 min |

---

## 🎯 Quick Navigation

### 👤 **For Users**
1. Read [README.md](README.md)
2. Follow [QUICK_START.md](QUICK_START.md)
3. Test API with [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)
4. Get help from [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### 👨‍💻 **For Developers**
1. Read [README.md](README.md) for architecture
2. Study [BEST_PRACTICES.md](BEST_PRACTICES.md)
3. Reference [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) for details
4. Contribute via [CONTRIBUTING.md](CONTRIBUTING.md)

### 🎓 **For Evaluators (Thesis/Banca)**
1. Executive summary in [README.md](README.md)
2. Complete analysis in [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md)
   - Requirements analysis (18/18 = 100%)
   - Architecture & design patterns
   - Security analysis
   - Test coverage (95.21%)
   - SOLID & clean code compliance
3. Verify implementation in source code: [`src/main/java`](src/main/java)

### 🔧 **For Maintainers**
1. Setup: [QUICK_START.md](QUICK_START.md)
2. Guidelines: [CONTRIBUTING.md](CONTRIBUTING.md)
3. Patterns: [BEST_PRACTICES.md](BEST_PRACTICES.md)
4. Issues: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

## 📊 Key Metrics at a Glance

| Metric | Value | Status |
|--------|-------|--------|
| **Requirements** | 18/18 (100%) | ✅ Complete |
| **Test Coverage** | 95.21% | ✅ Excellent |
| **Endpoints** | 7 RESTful | ✅ Complete |
| **Tests** | 53 (17 unit + 36 integration) | ✅ Comprehensive |
| **Architecture** | Clean Architecture | ✅ Implemented |
| **Security** | BCrypt strength 12, 3-layer validation | ✅ Robust |
| **API Standard** | RFC 7807 (ProblemDetail) | ✅ Compliant |
| **Status** | Production Ready | ✅ Approved |

---

## 🏗️ Architecture Overview

The system follows **Clean Architecture** principles with 5 layers:

```
┌─────────────────────────┐
│  REST API (Controllers) │ ← HTTP Entry point
├─────────────────────────┤
│  Business Logic Service │ ← Validation & Rules
├─────────────────────────┤
│  Data Access Repository │ ← Abstraction
├─────────────────────────┤
│  Domain Models (JPA)    │ ← Entities
├─────────────────────────┤
│  PostgreSQL Database    │ ← Persistence
└─────────────────────────┘
```

**See details:** [RELATORIO_TECNICO.md#4-arquitetura-da-solução](RELATORIO_TECNICO.md#4-arquitetura-da-solução)

---

## 🔌 Available Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/users` | Create user |
| GET | `/api/v1/users/{id}` | Get user by ID |
| GET | `/api/v1/users?name=` | Search by name |
| PUT | `/api/v1/users/{id}` | Update user |
| PATCH | `/api/v1/users/{id}/password` | Change password |
| DELETE | `/api/v1/users/{id}` | Delete user |
| POST | `/api/v1/auth/validate` | Validate credentials |

**Full details:** [API_TESTING_GUIDE.md](API_TESTING_GUIDE.md)

---

## 🚀 Quick Start

### Docker (Recommended)

```bash
docker-compose up --build
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Local Setup

```bash
mvn clean install
mvn spring-boot:run
```

**More details:** [QUICK_START.md](QUICK_START.md)

---

## 🧪 Test Coverage

- **53 tests** (17 unit + 36 integration)
- **95.21% code coverage** (JaCoCo)
- **100% endpoint coverage**
- **100% requirement fulfillment**

Run tests:
```bash
mvn clean verify
```

See details: [RELATORIO_TECNICO.md#8-testes-e-validação](RELATORIO_TECNICO.md#8-testes-e-validação)

---

## 🔐 Security Highlights

- ✅ **Password Security:** BCrypt strength 12 (~14 iterations/second)
- ✅ **Validation:** 3-layer approach (Bean Validation → Service → Database)
- ✅ **SQL Injection:** JPA parameterized queries
- ✅ **No Exposure:** Sensitive data never returned in responses
- ✅ **Error Handling:** RFC 7807 (ProblemDetail) standardized

Details: [RELATORIO_TECNICO.md#7-segurança](RELATORIO_TECNICO.md#7-segurança)

---

## 📖 Detailed Documentation

### Technical Report Structure

1. **Introduction** – Context and scope
2. **Methodology** – TDD, Agile, Clean Code
3. **Requirements Analysis** – 18/18 requirements mapped
4. **Architecture** – Clean Architecture, 5 layers, design patterns
5. **Implementation** – SQL schema, DTOs, validation
6. **API Specification** – 7 endpoints, RFC 7807, status codes
7. **Security** – Authn, encryption, validation, protection
8. **Testing** – Strategy, coverage (95.21%), execution
9. **Conformance** – REST, RFC 7807, OpenAPI 3.0, SOLID
10. **Metrics** – KPIs, performance, quality
11. **Lessons Learned** – Decisions and challenges
12. **Conclusions** – Results and next phases
13. **References** – Technologies and standards
14. **Appendices** – Files, commands, test structure

**Full report:** [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md)

---

## 📊 Diagrams (in RELATORIO_TECNICO.md)

The technical report includes Mermaid diagrams:

- 🏗️ **Architecture Diagram** – 5-layer system overview
- 🔄 **3-Layer Validation Flow** – Input validation strategy
- 🔐 **Authentication Sequence** – Login validation process
- 📝 **User Creation Flow** – Step-by-step creation process
- 🧪 **Test Pyramid** – Unit, integration, E2E distribution

---

## 🤝 Contributing

Want to contribute? Follow [CONTRIBUTING.md](CONTRIBUTING.md):

1. Fork the repository
2. Create a feature branch
3. Write tests (test-driven)
4. Commit with descriptive messages
5. Submit a pull request

Requirements:
- All tests must pass: `mvn test`
- Coverage ≥ 90%: `mvn jacoco:report`
- Follow code standards (see [BEST_PRACTICES.md](BEST_PRACTICES.md))

---

## ❓ Need Help?

Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for:
- PostgreSQL connection issues
- Maven/Java setup problems
- Spring Boot errors
- Docker issues
- Test failures
- API validation errors

---

## 📄 License

MIT License – See [LICENSE](LICENSE) file

---

## 👤 Author

**Israel Barberino**  
FIAP Tech Challenge – Phase 1  
January 2026

---

## 🔗 Repository

- **GitHub:** [seu-usuario/fiap-tech-challenge-1](https://github.com/seu-usuario/fiap-tech-challenge-1)
- **Main Branch:** `main`
- **Development:** `develop`

---

## 📞 Support

- 📖 **Documentation:** See guides above
- 🐛 **Report Issues:** GitHub Issues
- 💬 **Discussions:** GitHub Discussions
- 📧 **Email:** seu-email@example.com

---

**Last Updated:** January 3, 2026  
**Status:** ✅ Production Ready

---

## 🎯 Next Sections

- [README.md](README.md) – Project overview
- [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) – Complete technical analysis
- [QUICK_START.md](QUICK_START.md) – Get started in 5 minutes
