package com.fiap.challenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserManagementApplicationTest {

    @Test
    @DisplayName("Deve iniciar aplicacao pelo metodo main")
    void shouldStartApplicationViaMain() {
        assertDoesNotThrow(() -> UserManagementApplication.main(
            new String[] {"--spring.main.web-application-type=none", "--spring.main.banner-mode=off"}
        ));
    }
}
