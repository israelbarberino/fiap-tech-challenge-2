package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteMenuItemUseCase {

    private static final String MENU_ITEM_NOT_FOUND_MSG = "MenuItem not found with id: ";

    private final MenuItemGateway menuItemGateway;

    public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public void execute(Long id) {
        if (menuItemGateway.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(MENU_ITEM_NOT_FOUND_MSG + id);
        }
        menuItemGateway.deleteById(id);
    }
}
