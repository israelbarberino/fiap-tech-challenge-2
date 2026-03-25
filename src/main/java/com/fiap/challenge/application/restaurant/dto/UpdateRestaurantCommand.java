package com.fiap.challenge.application.restaurant.dto;

import com.fiap.challenge.entity.Address;

import java.time.LocalTime;

public record UpdateRestaurantCommand(
        String name,
        String cuisineType,
        LocalTime openingTime,
        LocalTime closingTime,
        Address address,
        Long ownerId
) {
}
