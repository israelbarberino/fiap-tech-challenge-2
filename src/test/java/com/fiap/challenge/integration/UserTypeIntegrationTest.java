package com.fiap.challenge.integration;

import com.fiap.challenge.dto.UserTypeRequestDTO;
import com.fiap.challenge.dto.UserTypeResponseDTO;
import com.fiap.challenge.repository.UserTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração - UserType")
class UserTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private UserTypeRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        userTypeRepository.deleteAll();
        requestDTO = new UserTypeRequestDTO("Dono de Restaurante", "Usuário que possui um restaurante");
    }

    @Test
    @DisplayName("Deve criar um tipo de usuário via POST")
    void testCreateUserTypeEndpoint() throws Exception {
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Dono de Restaurante")))
                .andExpect(jsonPath("$.description", is("Usuário que possui um restaurante")));
    }

    @Test
    @DisplayName("Deve buscar todos os tipos de usuários")
    void testGetAllUserTypesEndpoint() throws Exception {
        userTypeRepository.save(new com.fiap.challenge.entity.UserType("Dono de Restaurante", "Descrição"));
        userTypeRepository.save(new com.fiap.challenge.entity.UserType("Cliente", "Cliente do restaurante"));

        mockMvc.perform(get("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID")
    void testGetUserTypeByIdEndpoint() throws Exception {
        var userType = userTypeRepository.save(new com.fiap.challenge.entity.UserType("Dono de Restaurante", "Descrição"));

        mockMvc.perform(get("/api/v1/user-types/" + userType.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dono de Restaurante")));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar tipo de usuário inexistente")
    void testGetUserTypeNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user-types/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um tipo de usuário")
    void testUpdateUserTypeEndpoint() throws Exception {
        var userType = userTypeRepository.save(new com.fiap.challenge.entity.UserType("Dono de Restaurante", "Descrição"));
        UserTypeRequestDTO updateDTO = new UserTypeRequestDTO("Atualizado", "Nova descrição");
        String requestBody = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/v1/user-types/" + userType.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Atualizado")))
                .andExpect(jsonPath("$.description", is("Nova descrição")));
    }

    @Test
    @DisplayName("Deve deletar um tipo de usuário")
    void testDeleteUserTypeEndpoint() throws Exception {
        var userType = userTypeRepository.save(new com.fiap.challenge.entity.UserType("Dono de Restaurante", "Descrição"));

        mockMvc.perform(delete("/api/v1/user-types/" + userType.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/user-types/" + userType.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar erro ao criar tipo de usuário com dados inválidos")
    void testCreateUserTypeWithInvalidData() throws Exception {
        UserTypeRequestDTO invalidDTO = new UserTypeRequestDTO("", "Descrição");
        String requestBody = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
