package com.fiap.challenge.application.menuitem.dto;

import java.math.BigDecimal;

public record UpdateMenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        Boolean availableOnPremises,
        String photoPath,
        Long restaurantId
) {
}
