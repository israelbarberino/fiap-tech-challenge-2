package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginValidateRequest(
    @NotBlank(message = "Login é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    String password
) {
    public String getLogin() { return login; }
    public String getPassword() { return password; }
}
