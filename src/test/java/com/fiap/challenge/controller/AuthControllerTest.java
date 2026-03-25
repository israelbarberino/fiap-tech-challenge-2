package com.fiap.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.application.auth.usecase.ValidateLoginUseCase;
import com.fiap.challenge.dto.LoginValidateRequest;
import com.fiap.challenge.exception.InvalidLoginException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ValidateLoginUseCase validateLoginUseCase;

    @Test
    @DisplayName("Deve validar login com sucesso e retornar 200")
    void testValidateLoginSuccess() throws Exception {
        LoginValidateRequest request = new LoginValidateRequest("joao.silva", "senha123");
        doNothing().when(validateLoginUseCase).execute(any(LoginValidateRequest.class));

        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Credenciais válidas"));
    }

    @Test
    @DisplayName("Deve retornar 401 ao validar login com credenciais inválidas")
    void testValidateLoginInvalid() throws Exception {
        LoginValidateRequest request = new LoginValidateRequest("inexistente", "senhaErrada");
        doThrow(new InvalidLoginException("Login ou senha inválidos"))
            .when(validateLoginUseCase).execute(any(LoginValidateRequest.class));

        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode").value("INVALID_CREDENTIALS"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao validar login com dados inválidos")
    void testValidateLoginInvalidData() throws Exception {
        LoginValidateRequest request = new LoginValidateRequest("", "");

        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors.login").exists())
            .andExpect(jsonPath("$.validationErrors.password").exists());
    }
}
