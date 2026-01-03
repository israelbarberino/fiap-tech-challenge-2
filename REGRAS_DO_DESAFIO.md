# Tech Challenge – Fase 1  
## Regras e Requisitos Oficiais do Projeto

Este documento resume, de forma objetiva e fiel, as regras e exigências do **Tech Challenge – Fase 1**, conforme descrito no PDF oficial da Pós-Tech.

---

## 1. Objetivo da Fase 1

Desenvolver um **backend em Java com Spring Boot** responsável pela **gestão de usuários** de uma aplicação de marketplace de restaurantes.

> Nesta fase, **não é necessário implementar restaurantes, pedidos ou pagamentos**.  
> O foco exclusivo é **Usuário**.

---

## 2. Escopo Funcional Obrigatório

A aplicação **DEVE** implementar as seguintes funcionalidades:

### 2.1 Usuário
- Cadastrar usuário
- Atualizar dados do usuário (**sem senha**)
- Excluir usuário
- Buscar usuário por nome
- Garantir **unicidade de e-mail**
- Registrar **data da última alteração**
- Possuir **tipo de usuário**:
  - `RESTAURANT_OWNER`
  - `CUSTOMER`

### 2.2 Senha
- A troca de senha **DEVE** ocorrer em um **endpoint exclusivo**
- A senha **NÃO PODE** ser atualizada junto com outros dados do usuário

### 2.3 Validação de Login
- Criar um serviço para **validar login e senha**
- Retornar:
  - `200 OK` → credenciais válidas
  - `401 Unauthorized` → credenciais inválidas
- **Não é obrigatório** usar Spring Security nesta fase

---

## 3. Requisitos Técnicos Obrigatórios

### 3.1 Tecnologia
- Java
- Spring Boot
- Banco de dados **relacional**
  - MySQL **ou**
  - PostgreSQL

### 3.2 API
- API **versionada** (ex: `/api/v1`)
- Padrão REST
- Uso de DTOs para requests e responses

### 3.3 Tratamento de Erros
- Implementar tratamento global de exceções
- Utilizar **ProblemDetail (RFC 7807)** para respostas de erro
- Mapear corretamente:
  - `400` – Erros de validação
  - `404` – Recurso não encontrado
  - `409` – Conflito (ex: e-mail duplicado)
  - `401` – Login inválido

---

## 4. Documentação

### 4.1 Swagger
- A aplicação **DEVE** disponibilizar documentação via **Swagger UI**
- Todos os endpoints devem estar documentados

### 4.2 Postman
- Entregar uma **collection Postman (JSON)** contendo cenários principais:
  - Cadastro válido
  - Cadastro inválido
  - Atualização de dados
  - Troca de senha
  - Busca por nome
  - Validação de login (sucesso e erro)

---

## 5. Infraestrutura

- O projeto **DEVE** conter:
  - `Dockerfile` da aplicação
  - `docker-compose.yml` com:
    - Aplicação
    - Banco de dados
- O sistema deve ser executável via:
  ```bash
  docker-compose up
