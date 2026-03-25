package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.mapper.MenuItemApplicationMapper;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetMenuItemsByRestaurantUseCase {

    private final MenuItemGateway menuItemGateway;

    public GetMenuItemsByRestaurantUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItemResult> execute(Long restaurantId) {
        return menuItemGateway.findByRestaurantId(restaurantId).stream()
                .map(MenuItemApplicationMapper::toResult)
                .toList();
    }
}
