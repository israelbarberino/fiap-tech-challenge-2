package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public record UserTypeRequestDTO(
    @NotBlank(message = "Nome do tipo de usuário é obrigatório")
    String name,
    String description
) {}
