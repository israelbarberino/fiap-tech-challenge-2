package com.fiap.challenge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fiap.challenge.entity.Address;

import java.time.LocalTime;

public record RestaurantRequestDTO(
    @NotBlank(message = "Nome do restaurante é obrigatório")
    String name,
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    String cuisineType,
    @NotNull(message = "Horário de abertura é obrigatório")
    LocalTime openingTime,
    @NotNull(message = "Horário de fechamento é obrigatório")
    LocalTime closingTime,
    @Valid
    Address address,
    @NotNull(message = "ID do proprietário é obrigatório")
    Long ownerId
) {}
