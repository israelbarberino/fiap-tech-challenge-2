package com.fiap.challenge.domain.menuitem.port;

import com.fiap.challenge.domain.menuitem.model.MenuItemAggregate;

import java.util.List;
import java.util.Optional;

public interface MenuItemGateway {
    MenuItemAggregate save(MenuItemAggregate aggregate);

    Optional<MenuItemAggregate> findById(Long id);

    List<MenuItemAggregate> findAll();

    List<MenuItemAggregate> findByRestaurantId(Long restaurantId);

    void deleteById(Long id);
}
