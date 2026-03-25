package com.fiap.challenge.dto;

import com.fiap.challenge.entity.Address;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record RestaurantResponseDTO(
    Long id,
    String name,
    String cuisineType,
    LocalTime openingTime,
    LocalTime closingTime,
    Address address,
    Long ownerId,
    String ownerName,
    LocalDateTime createdAt,
    LocalDateTime lastModifiedAt
) {}
