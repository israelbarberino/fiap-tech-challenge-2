package com.fiap.challenge.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    @DisplayName("Deve preencher e ler campos de User e Address")
    void shouldPopulateUserAndAddress() {
        Address address = new Address("Rua A", "10", "Sala 1", "Sao Paulo", "SP", "01000-000");
        User user = new User("Joao", "joao@example.com", "joao", "hash", UserRole.CUSTOMER, address);

        user.setId(42L);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setLastModifiedAt(now);

        assertEquals(42L, user.getId());
        assertEquals("Joao", user.getName());
        assertEquals("joao@example.com", user.getEmail());
        assertEquals("joao", user.getLogin());
        assertEquals("hash", user.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, user.getRole());
        assertEquals(address, user.getAddress());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getLastModifiedAt());
        assertTrue(user.toString().contains("Joao"));

        Address emptyAddress = new Address();
        emptyAddress.setStreet("Rua B");
        emptyAddress.setNumber("20");
        emptyAddress.setComplement("Bloco C");
        emptyAddress.setCity("Santos");
        emptyAddress.setState("SP");
        emptyAddress.setZipCode("11000-000");

        assertEquals("Rua B", emptyAddress.getStreet());
        assertEquals("20", emptyAddress.getNumber());
        assertEquals("Bloco C", emptyAddress.getComplement());
        assertEquals("Santos", emptyAddress.getCity());
        assertEquals("SP", emptyAddress.getState());
        assertEquals("11000-000", emptyAddress.getZipCode());
    }
}
