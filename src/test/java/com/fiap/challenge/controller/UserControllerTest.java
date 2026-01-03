package com.fiap.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.dto.AddressDTO;
import com.fiap.challenge.dto.ChangePasswordRequest;
import com.fiap.challenge.dto.UserCreateRequest;
import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.dto.UserUpdateRequest;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.exception.UserNotFoundException;
import com.fiap.challenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private UserCreateRequest createRequest;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO(
            "Rua das Flores",
            "123",
            "Apto 456",
            "Sao Paulo",
            "SP",
            "01234-567"
        );

        userResponse = new UserResponse(
            1L,
            "Joao Silva",
            "joao@example.com",
            "joao.silva",
            UserRole.CUSTOMER,
            addressDTO,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        createRequest = new UserCreateRequest(
            "Joao Silva",
            "joao@example.com",
            "joao.silva",
            "senha123",
            UserRole.CUSTOMER,
            addressDTO
        );
    }

    @Test
    @DisplayName("Deve criar usuario e retornar 201")
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserCreateRequest.class)))
            .thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Joao Silva"))
            .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao criar usuario com dados invalidos")
    void testCreateUserWithInvalidData() throws Exception {
        UserCreateRequest invalidRequest = new UserCreateRequest(
            "",
            "email-invalido",
            "",
            "",
            UserRole.CUSTOMER,
            addressDTO
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar usuario e retornar 200")
    void testUpdateUser() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "Joao Silva Updated",
            "joao.updated@example.com",
            UserRole.RESTAURANT_OWNER,
            addressDTO
        );

        when(userService.updateUser(anyLong(), any(UserUpdateRequest.class)))
            .thenReturn(userResponse);

        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Joao Silva"));
    }

    @Test
    @DisplayName("Deve alterar senha e retornar 204")
    void testChangePassword() throws Exception {
        ChangePasswordRequest changeRequest = new ChangePasswordRequest(
            "senha123",
            "novaSenha456",
            "novaSenha456"
        );

        doNothing().when(userService).changePassword(anyLong(), any(ChangePasswordRequest.class));

        mockMvc.perform(patch("/api/v1/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve obter usuario por ID e retornar 200")
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L))
            .thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Joao Silva"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao obter usuario inexistente")
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(999L))
            .thenThrow(new UserNotFoundException("Usuario nao encontrado"));

        mockMvc.perform(get("/api/v1/users/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    @Test
    @DisplayName("Deve buscar usuarios por nome e retornar 200")
    void testGetUsersByName() throws Exception {
        when(userService.getUsersByName("Joao"))
            .thenReturn(List.of(userResponse));

        mockMvc.perform(get("/api/v1/users?name=Joao")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Joao Silva"))
            .andExpect(jsonPath("$[0].role").value("CUSTOMER"));
    }

    @Test
    @DisplayName("Deve deletar usuario e retornar 204")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
