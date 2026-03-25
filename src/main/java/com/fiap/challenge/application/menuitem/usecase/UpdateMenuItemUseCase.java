package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.dto.UpdateMenuItemCommand;
import com.fiap.challenge.application.menuitem.mapper.MenuItemApplicationMapper;
import com.fiap.challenge.domain.menuitem.model.MenuItemAggregate;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateMenuItemUseCase {

    private static final String MENU_ITEM_NOT_FOUND_MSG = "MenuItem not found with id: ";

    private final MenuItemGateway menuItemGateway;

    public UpdateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItemResult execute(Long id, UpdateMenuItemCommand command) {
        MenuItemAggregate current = menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_ITEM_NOT_FOUND_MSG + id));

        MenuItemAggregate aggregate = new MenuItemAggregate(
                current.id(),
                command.name(),
                command.description(),
                command.price(),
                command.availableOnPremises(),
                command.photoPath(),
                command.restaurantId(),
                current.restaurantName(),
                current.createdAt(),
                current.lastModifiedAt()
        );

        return MenuItemApplicationMapper.toResult(menuItemGateway.save(aggregate));
    }
}
