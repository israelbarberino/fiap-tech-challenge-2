package com.fiap.challenge.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRoleTest {

    @Test
    @DisplayName("Deve retornar o nome de exibicao do papel")
    void shouldReturnDisplayName() {
        assertEquals("Proprietário de Restaurante", UserRole.RESTAURANT_OWNER.getDisplayName());
        assertEquals("Cliente", UserRole.CUSTOMER.getDisplayName());
    }
}
