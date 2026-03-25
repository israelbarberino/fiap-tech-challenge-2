package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.application.menuitem.dto.CreateMenuItemCommand;
import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.mapper.MenuItemApplicationMapper;
import com.fiap.challenge.domain.menuitem.model.MenuItemAggregate;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public CreateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItemResult execute(CreateMenuItemCommand command) {
        MenuItemAggregate aggregate = new MenuItemAggregate(
                null,
                command.name(),
                command.description(),
                command.price(),
                command.availableOnPremises(),
                command.photoPath(),
                command.restaurantId(),
                null,
                null,
                null
        );

        return MenuItemApplicationMapper.toResult(menuItemGateway.save(aggregate));
    }
}
