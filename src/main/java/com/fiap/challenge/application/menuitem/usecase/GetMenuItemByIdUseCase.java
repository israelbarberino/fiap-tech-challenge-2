package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.mapper.MenuItemApplicationMapper;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetMenuItemByIdUseCase {

    private static final String MENU_ITEM_NOT_FOUND_MSG = "MenuItem not found with id: ";

    private final MenuItemGateway menuItemGateway;

    public GetMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItemResult execute(Long id) {
        return menuItemGateway.findById(id)
                .map(MenuItemApplicationMapper::toResult)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_ITEM_NOT_FOUND_MSG + id));
    }
}
