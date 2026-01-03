package com.fiap.challenge.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.IntegrationTest;
import com.fiap.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@DisplayName("Testes E2E (TAAC) - Fluxos Completos")
class UserManagementE2ETest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String API_PATH = "/api/v1";
    private static final String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("E2E: Fluxo completo de criação, validação e atualização de usuário")
    void testCompleteUserLifecycle() throws Exception {
        String createUserBody = """
            {
              "name": "Maria Santos",
              "email": "maria.santos@example.com",
              "login": "maria.santos",
              "password": "senha123@Abc",
              "role": "CUSTOMER",
              "address": {
                "street": "Avenida Brasil",
                "number": "500",
                "complement": "Sala 10",
                "city": "Rio de Janeiro",
                "state": "RJ",
                "zipCode": "20040020"
              }
            }
            """;

        var createResult = mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(createUserBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name", equalTo("Maria Santos")))
            .andExpect(jsonPath("$.email", equalTo("maria.santos@example.com")))
            .andExpect(jsonPath("$.role", equalTo("CUSTOMER")))
            .andReturn();

        Long userId = extractUserId(createResult.getResponse().getContentAsString());

        String loginValidateBody = """
            {
              "login": "maria.santos",
              "password": "senha123@Abc"
            }
            """;

        mockMvc.perform(post(API_PATH + "/auth/validate")
                .contentType(CONTENT_TYPE)
                .content(loginValidateBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", equalTo("Credenciais válidas")));

        mockMvc.perform(get(API_PATH + "/users/" + userId)
                .contentType(CONTENT_TYPE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(userId.intValue())))
            .andExpect(jsonPath("$.email", equalTo("maria.santos@example.com")));

        String updateUserBody = """
            {
              "name": "Maria Santos Silva",
              "email": "maria.silva@example.com",
              "role": "RESTAURANT_OWNER",
              "address": {
                "street": "Avenida Atlantica",
                "number": "1000",
                "complement": "Apto 1001",
                "city": "Rio de Janeiro",
                "state": "RJ",
                "zipCode": "20000000"
              }
            }
            """;

        mockMvc.perform(put(API_PATH + "/users/" + userId)
                .contentType(CONTENT_TYPE)
                .content(updateUserBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", equalTo("Maria Santos Silva")))
            .andExpect(jsonPath("$.email", equalTo("maria.silva@example.com")))
            .andExpect(jsonPath("$.role", equalTo("RESTAURANT_OWNER")));

        String changePasswordBody = """
            {
              "currentPassword": "senha123@Abc",
              "newPassword": "novaSenha456@Xyz",
              "confirmPassword": "novaSenha456@Xyz"
            }
            """;

        mockMvc.perform(patch(API_PATH + "/users/" + userId + "/password")
                .contentType(CONTENT_TYPE)
                .content(changePasswordBody))
            .andExpect(status().isNoContent());

        String newLoginValidateBody = """
            {
              "login": "maria.santos",
              "password": "novaSenha456@Xyz"
            }
            """;

        mockMvc.perform(post(API_PATH + "/auth/validate")
                .contentType(CONTENT_TYPE)
                .content(newLoginValidateBody))
            .andExpect(status().isOk());

        mockMvc.perform(delete(API_PATH + "/users/" + userId)
                .contentType(CONTENT_TYPE))
            .andExpect(status().isNoContent());

        mockMvc.perform(get(API_PATH + "/users/" + userId)
                .contentType(CONTENT_TYPE))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("E2E: Fluxo de validação de dados inválidos")
    void testValidationErrorFlow() throws Exception {
        String invalidEmailBody = """
            {
              "name": "Test User",
              "email": "email-invalido",
              "login": "testuser",
              "password": "senha123",
              "role": "CUSTOMER",
              "address": {
                "street": "Rua Test",
                "number": "123",
                "city": "São Paulo",
                "state": "SP",
                "zipCode": "01234-567"
              }
            }
            """;

        mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(invalidEmailBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors.email", notNullValue()));

        String emptyEmailBody = """
            {
              "name": "Test User",
              "email": "",
              "login": "testuser",
              "password": "senha123",
              "role": "CUSTOMER",
              "address": {
                "street": "Rua Test",
                "number": "123",
                "city": "São Paulo",
                "state": "SP",
                "zipCode": "01234-567"
              }
            }
            """;

        mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(emptyEmailBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors", hasKey("email")));
    }

    @Test
    @DisplayName("E2E: Fluxo de prevenção de duplicatas")
    void testDuplicateDataFlow() throws Exception {
        String createFirstBody = """
            {
              "name": "User First",
              "email": "duplicate@example.com",
              "login": "firstlogin",
              "password": "senha123",
              "role": "CUSTOMER",
              "address": {
                "street": "Rua Test",
                "number": "123",
                "city": "São Paulo",
                "state": "SP",
                "zipCode": "01234-567"
              }
            }
            """;

        mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(createFirstBody))
            .andExpect(status().isCreated());

        String createSecondBody = """
            {
              "name": "User Second",
              "email": "duplicate@example.com",
              "login": "secondlogin",
              "password": "senha123",
              "role": "CUSTOMER",
              "address": {
                "street": "Rua Test",
                "number": "123",
                "city": "São Paulo",
                "state": "SP",
                "zipCode": "01234-567"
              }
            }
            """;

        mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(createSecondBody))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorCode", equalTo("DUPLICATE_EMAIL")));
    }

    @Test
    @DisplayName("E2E: Fluxo de busca e filtragem")
    void testSearchAndFilterFlow() throws Exception {
        createMultipleUsers();

        mockMvc.perform(get(API_PATH + "/users?name=Joao")
                .contentType(CONTENT_TYPE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].name", containsString("Joao")));
    }

    @Test
    @DisplayName("E2E: Fluxo de autenticação inválida")
    void testInvalidAuthenticationFlow() throws Exception {
        createTestUser("auth.user", "auth.user@example.com", "senha123");

        String invalidLoginBody = """
            {
              "login": "inexistente",
              "password": "senha123"
            }
            """;

        mockMvc.perform(post(API_PATH + "/auth/validate")
                .contentType(CONTENT_TYPE)
                .content(invalidLoginBody))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode", equalTo("INVALID_CREDENTIALS")));

        String wrongPasswordBody = """
            {
              "login": "auth.user",
              "password": "senhaerrada"
            }
            """;

        mockMvc.perform(post(API_PATH + "/auth/validate")
                .contentType(CONTENT_TYPE)
                .content(wrongPasswordBody))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("E2E: Fluxo de alteração de senha com validação")
    void testPasswordChangeValidationFlow() throws Exception {
        Long userId = createTestUser("pwd.user", "pwd.user@example.com", "senha123");

        String wrongCurrentPasswordBody = """
            {
              "currentPassword": "senhaerrada",
              "newPassword": "novaSenha456",
              "confirmPassword": "novaSenha456"
            }
            """;

        mockMvc.perform(patch(API_PATH + "/users/" + userId + "/password")
                .contentType(CONTENT_TYPE)
                .content(wrongCurrentPasswordBody))
            .andExpect(status().isUnauthorized());

        String mismatchPasswordBody = """
            {
              "currentPassword": "senha123",
              "newPassword": "novaSenha456",
              "confirmPassword": "senhadiferente"
            }
            """;

        mockMvc.perform(patch(API_PATH + "/users/" + userId + "/password")
                .contentType(CONTENT_TYPE)
                .content(mismatchPasswordBody))
            .andExpect(status().isBadRequest());
    }

    private Long createTestUser(String login, String email, String password) throws Exception {
        String createBody = String.format("""
            {
              "name": "Test %s",
              "email": "%s",
              "login": "%s",
              "password": "%s",
              "role": "CUSTOMER",
              "address": {
                "street": "Rua Test",
                "number": "123",
                "city": "São Paulo",
                "state": "SP",
                "zipCode": "01234-567"
              }
            }
            """, login, email, login, password);

        var result = mockMvc.perform(post(API_PATH + "/users")
                .contentType(CONTENT_TYPE)
                .content(createBody))
            .andExpect(status().isCreated())
            .andReturn();

        return extractUserId(result.getResponse().getContentAsString());
    }

    private void createMultipleUsers() throws Exception {
        String[] names = {"Joao Silva", "Maria Santos", "Joao Oliveira", "Pedro Costa"};

        for (String name : names) {
            String[] parts = name.split(" ");
            String login = (parts[0] + "." + parts[1]).toLowerCase();
            String email = login + "@example.com";

            String createBody = String.format("""
                {
                  "name": "%s",
                  "email": "%s",
                  "login": "%s",
                  "password": "senha123",
                  "role": "CUSTOMER",
                  "address": {
                    "street": "Rua Test",
                    "number": "123",
                    "city": "São Paulo",
                    "state": "SP",
                    "zipCode": "01234-567"
                  }
                }
                """, name, email, login);

            mockMvc.perform(post(API_PATH + "/users")
                    .contentType(CONTENT_TYPE)
                    .content(createBody))
                .andExpect(status().isCreated());
        }
    }

    private Long extractUserId(String jsonResponse) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonResponse);
            if (root.path("id").isMissingNode()) {
                throw new IllegalStateException("Campo 'id' ausente na resposta: " + jsonResponse);
            }
            return root.path("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao extrair ID da resposta", e);
        }
    }
}
