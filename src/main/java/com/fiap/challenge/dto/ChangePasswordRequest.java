package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
    @NotBlank(message = "Senha atual é obrigatória")
    String currentPassword,

    @NotBlank(message = "Nova senha é obrigatória")
    String newPassword,

    @NotBlank(message = "Confirmação de senha é obrigatória")
    String confirmPassword
) {
    public String getCurrentPassword() { return currentPassword; }
    public String getNewPassword() { return newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
}
