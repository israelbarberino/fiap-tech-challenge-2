package com.fiap.challenge.application.menuitem.mapper;

import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.domain.menuitem.model.MenuItemAggregate;

public final class MenuItemApplicationMapper {

    private MenuItemApplicationMapper() {
    }

    public static MenuItemResult toResult(MenuItemAggregate aggregate) {
        return new MenuItemResult(
                aggregate.id(),
                aggregate.name(),
                aggregate.description(),
                aggregate.price(),
                aggregate.availableOnPremises(),
                aggregate.photoPath(),
                aggregate.restaurantId(),
                aggregate.restaurantName(),
                aggregate.createdAt(),
                aggregate.lastModifiedAt()
        );
    }
}
