package com.fiap.challenge.dto;

import com.fiap.challenge.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotBlank(message = "Email é obrigatório")
    @Email(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Email inválido: deve estar no formato correto (ex: usuario@exemplo.com)"
    )
    String email,

    @NotBlank(message = "Login é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    String password,

    @NotNull(message = "Papel do usuário é obrigatório")
    UserRole role,

    @NotNull(message = "Endereço é obrigatório")
    AddressDTO address
) {
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public AddressDTO getAddress() { return address; }
}
