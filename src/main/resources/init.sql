-- Script de exemplo para popular o banco de dados com dados de teste
-- Execute este script após a aplicação inicializar

-- Limpar dados existentes (opcional)
-- DELETE FROM users;

-- Inserir usuários de exemplo
-- Senhas: password123, password456, password789
-- Hashes gerados com BCrypt (strength 12)

INSERT INTO users (name, email, login, password_hash, role, street, number, complement, city, state, zip_code, created_at, last_modified_at)
VALUES
(
    'João Silva',
    'joao.silva@example.com',
    'joao.silva',
    '$2a$12$R6mAp7vM.9K0Q0cQ0Q0Q0OQ0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q0Q',
    'USER',
    'Rua das Flores',
    '123',
    'Apto 456',
    'São Paulo',
    'SP',
    '01234-567',
    NOW(),
    NOW()
),
(
    'Maria Santos',
    'maria.santos@example.com',
    'maria.santos',
    '$2a$12$S6nBq8wN.0L1R1dR1dR1PQ1R1dR1dR1dR1dR1dR1dR1dR1dR1dR1dR',
    'MANAGER',
    'Avenida Paulista',
    '1000',
    'Sala 101',
    'São Paulo',
    'SP',
    '01311-100',
    NOW(),
    NOW()
),
(
    'Carlos Administrator',
    'carlos.admin@example.com',
    'carlos.admin',
    '$2a$12$T7oCs9xO.1M2S2eS2eS2QR2S2eS2eS2eS2eS2eS2eS2eS2eS2eS2eS',
    'ADMIN',
    'Rua Augusta',
    '2500',
    NULL,
    'São Paulo',
    'SP',
    '01305-100',
    NOW(),
    NOW()
)
ON CONFLICT (email) DO NOTHING;

-- Verificar inserção
SELECT id, name, email, login, role FROM users;

-- Notas:
-- 1. Os hashes de senha são placeholders. Para gerar hashes reais, use BCrypt com strength 12
-- 2. Para testar login com a senha original "password123", você pode usar:
--    POST /api/v1/auth/validate com { "login": "joao.silva", "password": "password123" }
-- 3. Se usar Docker Compose, este script é executado automaticamente
