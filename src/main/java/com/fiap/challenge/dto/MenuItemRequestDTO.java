package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MenuItemRequestDTO(
    @NotBlank(message = "Nome do item é obrigatório")
    String name,
    String description,
    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço não pode ser negativo")
    BigDecimal price,
    @NotNull(message = "Disponibilidade no restaurante é obrigatória")
    Boolean availableOnPremises,
    String photoPath,
    @NotNull(message = "ID do restaurante é obrigatório")
    Long restaurantId
) {}
