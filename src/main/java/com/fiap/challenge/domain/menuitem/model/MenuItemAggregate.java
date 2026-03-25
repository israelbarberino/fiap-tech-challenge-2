package com.fiap.challenge.domain.menuitem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MenuItemAggregate(
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
