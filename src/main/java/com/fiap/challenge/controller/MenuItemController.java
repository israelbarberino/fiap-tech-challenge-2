package com.fiap.challenge.controller;

import com.fiap.challenge.dto.MenuItemRequestDTO;
import com.fiap.challenge.dto.MenuItemResponseDTO;
import com.fiap.challenge.service.MenuItemService;
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

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    /**
     * Cria um novo item de cardápio.
     * POST /api/v1/menu-items
     */
    @PostMapping
    @Operation(summary = "Criar novo item de cardápio")
    public ResponseEntity<MenuItemResponseDTO> create(@Valid @RequestBody MenuItemRequestDTO requestDTO) {
        MenuItemResponseDTO responseDTO = menuItemService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Busca um item de cardápio por ID.
     * GET /api/v1/menu-items/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar item de cardápio por ID")
    public ResponseEntity<MenuItemResponseDTO> getById(@PathVariable Long id) {
        MenuItemResponseDTO responseDTO = menuItemService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os itens de cardápio.
     * GET /api/v1/menu-items
     */
    @GetMapping
    @Operation(summary = "Listar todos os itens de cardápio")
    public ResponseEntity<List<MenuItemResponseDTO>> getAll() {
        List<MenuItemResponseDTO> responseDTOs = menuItemService.getAll();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Lista itens de cardápio de um restaurante específico.
     * GET /api/v1/menu-items/restaurant/{restaurantId}
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Listar itens de cardápio de um restaurante")
    public ResponseEntity<List<MenuItemResponseDTO>> getByRestaurantId(@PathVariable Long restaurantId) {
        List<MenuItemResponseDTO> responseDTOs = menuItemService.getByRestaurantId(restaurantId);
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
        MenuItemResponseDTO responseDTO = menuItemService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deleta um item de cardápio.
     * DELETE /api/v1/menu-items/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item de cardápio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
