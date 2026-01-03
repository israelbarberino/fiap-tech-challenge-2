.PHONY: help build run test clean docker-build docker-up docker-down

help:
	@echo "User Management API - Makefile Commands"
	@echo ""
	@echo "Usage: make [command]"
	@echo ""
	@echo "Commands:"
	@echo "  build          - Compilar o projeto Maven"
	@echo "  test           - Executar testes unitários"
	@echo "  clean          - Limpar artifacts do Maven"
	@echo "  run            - Executar aplicação localmente"
	@echo "  package        - Empacotar aplicação como JAR"
	@echo "  docker-build   - Construir imagem Docker"
	@echo "  docker-up      - Iniciar containers (Docker Compose)"
	@echo "  docker-down    - Parar containers (Docker Compose)"
	@echo "  docker-logs    - Ver logs dos containers"
	@echo "  swagger        - Abrir Swagger UI no navegador"
	@echo "  help           - Mostrar esta mensagem"

build:
	@echo "Compilando projeto..."
	mvn clean compile

test:
	@echo "Executando testes..."
	mvn test

clean:
	@echo "Limpando artifacts..."
	mvn clean

run:
	@echo "Executando aplicação..."
	mvn spring-boot:run

package:
	@echo "Empacotando aplicação..."
	mvn clean package -DskipTests

docker-build:
	@echo "Construindo imagem Docker..."
	docker build -t user-management-api:latest .

docker-up:
	@echo "Iniciando containers..."
	docker-compose up -d

docker-down:
	@echo "Parando containers..."
	docker-compose down

docker-logs:
	@echo "Logs da aplicação:"
	docker-compose logs -f app

docker-logs-db:
	@echo "Logs do banco de dados:"
	docker-compose logs -f postgres

swagger:
	@echo "Abrindo Swagger UI..."
	start http://localhost:8080/swagger-ui.html

# Aliases úteis
install: build

start: docker-up

stop: docker-down
