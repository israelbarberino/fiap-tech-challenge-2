package com.fiap.challenge.dto;

import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    @DisplayName("Deve mapear usuario com endereco")
    void shouldMapUserWithAddress() {
        Address address = new Address("Rua A", "10", "Sala 1", "Sao Paulo", "SP", "01000-000");
        User user = new User("Joao", "joao@example.com", "joao", "hash", UserRole.CUSTOMER, address);
        user.setId(5L);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setLastModifiedAt(now);

        UserResponse response = new UserResponse(user);

        assertEquals(5L, response.getId());
        assertEquals("Joao", response.getName());
        assertEquals("joao@example.com", response.getEmail());
        assertEquals("joao", response.getLogin());
        assertEquals(UserRole.CUSTOMER, response.getRole());
        assertEquals("Rua A", response.getAddress().getStreet());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getLastModifiedAt());
    }

    @Test
    @DisplayName("Deve retornar endereco nulo quando nao fornecido")
    void shouldHandleNullAddress() {
        User user = new User("Maria", "maria@example.com", "maria", "hash", UserRole.RESTAURANT_OWNER, null);

        UserResponse response = new UserResponse(user);

        assertNull(response.getAddress());
        assertEquals(UserRole.RESTAURANT_OWNER, response.getRole());
    }
}
