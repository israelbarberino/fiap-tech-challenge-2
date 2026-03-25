package com.fiap.challenge.controller;

import com.fiap.challenge.application.menuitem.dto.CreateMenuItemCommand;
import com.fiap.challenge.application.menuitem.dto.MenuItemResult;
import com.fiap.challenge.application.menuitem.dto.UpdateMenuItemCommand;
import com.fiap.challenge.application.menuitem.usecase.CreateMenuItemUseCase;
import com.fiap.challenge.application.menuitem.usecase.DeleteMenuItemUseCase;
import com.fiap.challenge.application.menuitem.usecase.GetAllMenuItemsUseCase;
import com.fiap.challenge.application.menuitem.usecase.GetMenuItemByIdUseCase;
import com.fiap.challenge.application.menuitem.usecase.GetMenuItemsByRestaurantUseCase;
import com.fiap.challenge.application.menuitem.usecase.UpdateMenuItemUseCase;
import com.fiap.challenge.dto.MenuItemRequestDTO;
import com.fiap.challenge.dto.MenuItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar itens do cardápio.
 * Fornece endpoints REST para operações CRUD em itens do cardápio.
 */
@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "Menu Items", description = "Endpoints para gerenciar itens do cardápio")
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final GetAllMenuItemsUseCase getAllMenuItemsUseCase;
    private final GetMenuItemsByRestaurantUseCase getMenuItemsByRestaurantUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    public MenuItemController(
            CreateMenuItemUseCase createMenuItemUseCase,
            GetMenuItemByIdUseCase getMenuItemByIdUseCase,
            GetAllMenuItemsUseCase getAllMenuItemsUseCase,
            GetMenuItemsByRestaurantUseCase getMenuItemsByRestaurantUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.getMenuItemByIdUseCase = getMenuItemByIdUseCase;
        this.getAllMenuItemsUseCase = getAllMenuItemsUseCase;
        this.getMenuItemsByRestaurantUseCase = getMenuItemsByRestaurantUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
    }

    /**
     * Cria um novo item de cardápio.
     * POST /api/v1/menu-items
     */
    @PostMapping
    @Operation(summary = "Criar novo item de cardápio")
    public ResponseEntity<MenuItemResponseDTO> create(@Valid @RequestBody MenuItemRequestDTO requestDTO) {
        MenuItemResult result = createMenuItemUseCase.execute(new CreateMenuItemCommand(
                requestDTO.name(),
                requestDTO.description(),
                requestDTO.price(),
                requestDTO.availableOnPremises(),
                requestDTO.photoPath(),
                requestDTO.restaurantId()
        ));
        MenuItemResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Busca um item de cardápio por ID.
     * GET /api/v1/menu-items/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar item de cardápio por ID")
    public ResponseEntity<MenuItemResponseDTO> getById(@PathVariable Long id) {
        MenuItemResponseDTO responseDTO = toResponseDTO(getMenuItemByIdUseCase.execute(id));
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os itens de cardápio.
     * GET /api/v1/menu-items
     */
    @GetMapping
    @Operation(summary = "Listar todos os itens de cardápio")
    public ResponseEntity<List<MenuItemResponseDTO>> getAll() {
        List<MenuItemResponseDTO> responseDTOs = getAllMenuItemsUseCase.execute().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Lista itens de cardápio de um restaurante específico.
     * GET /api/v1/menu-items/restaurant/{restaurantId}
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Listar itens de cardápio de um restaurante")
    public ResponseEntity<List<MenuItemResponseDTO>> getByRestaurantId(@PathVariable Long restaurantId) {
        List<MenuItemResponseDTO> responseDTOs = getMenuItemsByRestaurantUseCase.execute(restaurantId).stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Atualiza um item de cardápio existente.
     * PUT /api/v1/menu-items/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de cardápio")
    public ResponseEntity<MenuItemResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequestDTO requestDTO) {
        MenuItemResult result = updateMenuItemUseCase.execute(id, new UpdateMenuItemCommand(
            requestDTO.name(),
            requestDTO.description(),
            requestDTO.price(),
            requestDTO.availableOnPremises(),
            requestDTO.photoPath(),
            requestDTO.restaurantId()
        ));
        MenuItemResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deleta um item de cardápio.
     * DELETE /api/v1/menu-items/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item de cardápio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private MenuItemResponseDTO toResponseDTO(MenuItemResult result) {
        return new MenuItemResponseDTO(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                result.availableOnPremises(),
                result.photoPath(),
                result.restaurantId(),
                result.restaurantName(),
                result.createdAt(),
                result.lastModifiedAt()
        );
    }
}
