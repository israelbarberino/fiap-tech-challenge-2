#!/bin/bash
# Script de inicialização rápida do projeto

echo "╔════════════════════════════════════════════════════════╗"
echo "║  User Management API - Spring Boot 3 + Java 17       ║"
echo "║  FIAP Tech Challenge 1                                ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Verificar requisitos
echo -e "${BLUE}[*] Verificando requisitos...${NC}"

# Verificar Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[^"]+')
    echo -e "${GREEN}[✓] Java $JAVA_VERSION encontrado${NC}"
else
    echo -e "${RED}[✗] Java não encontrado. Instale Java 17+${NC}"
    exit 1
fi

# Verificar Maven
if command -v mvn &> /dev/null; then
    MAVEN_VERSION=$(mvn -version 2>&1 | grep "Apache Maven" | grep -oP '[0-9]+\.[0-9]+\.[0-9]+')
    echo -e "${GREEN}[✓] Maven $MAVEN_VERSION encontrado${NC}"
else
    echo -e "${RED}[✗] Maven não encontrado. Instale Maven 3.6+${NC}"
    exit 1
fi

# Verificar Docker (opcional)
if command -v docker &> /dev/null; then
    echo -e "${GREEN}[✓] Docker encontrado${NC}"
else
    echo -e "${YELLOW}[!] Docker não encontrado (opcional)${NC}"
fi

echo ""
echo -e "${BLUE}[*] Opções de inicialização:${NC}"
echo ""
echo "1. Compilar e testar o projeto"
echo "2. Executar aplicação localmente (requer PostgreSQL)"
echo "3. Iniciar com Docker Compose"
echo "4. Abrir documentação (Swagger)"
echo "5. Limpar projeto"
echo "6. Executar testes"
echo "0. Sair"
echo ""

read -p "Escolha uma opção [0-6]: " choice

case $choice in
    1)
        echo -e "${BLUE}[*] Compilando projeto...${NC}"
        mvn clean compile
        echo -e "${GREEN}[✓] Compilação concluída!${NC}"
        ;;
    2)
        echo -e "${BLUE}[*] Verificando PostgreSQL...${NC}"
        if ! pgrep -x "postgres" > /dev/null && ! docker ps | grep -q postgres; then
            echo -e "${RED}[✗] PostgreSQL não está rodando!${NC}"
            echo -e "${YELLOW}Use: docker-compose up postgres -d${NC}"
            exit 1
        fi
        echo -e "${GREEN}[✓] PostgreSQL OK${NC}"
        echo -e "${BLUE}[*] Iniciando aplicação...${NC}"
        mvn spring-boot:run
        ;;
    3)
        echo -e "${BLUE}[*] Verificando Docker...${NC}"
        if ! command -v docker &> /dev/null; then
            echo -e "${RED}[✗] Docker não instalado!${NC}"
            exit 1
        fi
        echo -e "${GREEN}[✓] Docker OK${NC}"
        echo -e "${BLUE}[*] Iniciando containers...${NC}"
        docker-compose up --build
        ;;
    4)
        echo -e "${BLUE}[*] Abrindo Swagger UI...${NC}"
        if command -v xdg-open &> /dev/null; then
            xdg-open http://localhost:8080/swagger-ui.html
        elif command -v open &> /dev/null; then
            open http://localhost:8080/swagger-ui.html
        else
            echo -e "${YELLOW}Acesse: http://localhost:8080/swagger-ui.html${NC}"
        fi
        ;;
    5)
        echo -e "${BLUE}[*] Limpando projeto...${NC}"
        mvn clean
        echo -e "${GREEN}[✓] Projeto limpo!${NC}"
        ;;
    6)
        echo -e "${BLUE}[*] Executando testes...${NC}"
        mvn test
        ;;
    0)
        echo -e "${YELLOW}Saindo...${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}Opção inválida!${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}[✓] Operação concluída!${NC}"
echo ""
echo -e "${BLUE}Próximos passos:${NC}"
echo "1. Documentação: Veja README.md"
echo "2. API Tests: Veja API_TESTING_GUIDE.md"
echo "3. Troubleshooting: Veja TROUBLESHOOTING.md"
echo "4. Best Practices: Veja BEST_PRACTICES.md"
echo ""
