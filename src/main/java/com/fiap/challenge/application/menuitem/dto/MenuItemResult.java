package com.fiap.challenge.application.menuitem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MenuItemResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean availableOnPremises,
        String photoPath,
        Long restaurantId,
        String restaurantName,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {
}
