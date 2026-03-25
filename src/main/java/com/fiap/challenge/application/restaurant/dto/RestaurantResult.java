package com.fiap.challenge.application.restaurant.dto;

import com.fiap.challenge.entity.Address;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record RestaurantResult(
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
) {
}
