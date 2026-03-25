package com.fiap.challenge.application.menuitem.usecase;

import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.mapper.MenuItemApplicationMapper;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllMenuItemsUseCase {

    private final MenuItemGateway menuItemGateway;

    public GetAllMenuItemsUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItemResult> execute() {
        return menuItemGateway.findAll().stream()
                .map(MenuItemApplicationMapper::toResult)
                .toList();
    }
}
