package com.fiap.challenge.controller;

import com.fiap.challenge.dto.RestaurantRequestDTO;
import com.fiap.challenge.dto.RestaurantResponseDTO;
import com.fiap.challenge.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar restaurantes.
 * Fornece endpoints REST para operações CRUD em restaurantes.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurants", description = "Endpoints para gerenciar restaurantes")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Cria um novo restaurante.
     * POST /api/v1/restaurants
     */
    @PostMapping
    @Operation(summary = "Criar novo restaurante")
    public ResponseEntity<RestaurantResponseDTO> create(@Valid @RequestBody RestaurantRequestDTO requestDTO) {
        RestaurantResponseDTO responseDTO = restaurantService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Busca um restaurante por ID.
     * GET /api/v1/restaurants/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID")
    public ResponseEntity<RestaurantResponseDTO> getById(@PathVariable Long id) {
        RestaurantResponseDTO responseDTO = restaurantService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um restaurante por nome.
     * GET /api/v1/restaurants/name/{name}
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar restaurante por nome")
    public ResponseEntity<RestaurantResponseDTO> getByName(@PathVariable String name) {
        RestaurantResponseDTO responseDTO = restaurantService.getByName(name);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os restaurantes.
     * GET /api/v1/restaurants
     */
    @GetMapping
    @Operation(summary = "Listar todos os restaurantes")
    public ResponseEntity<List<RestaurantResponseDTO>> getAll() {
        List<RestaurantResponseDTO> responseDTOs = restaurantService.getAll();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Lista restaurantes por tipo de cozinha.
     * GET /api/v1/restaurants/cuisine/{cuisineType}
     */
    @GetMapping("/cuisine/{cuisineType}")
    @Operation(summary = "Listar restaurantes por tipo de cozinha")
    public ResponseEntity<List<RestaurantResponseDTO>> getByCuisineType(@PathVariable String cuisineType) {
        List<RestaurantResponseDTO> responseDTOs = restaurantService.getByCuisineType(cuisineType);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Atualiza um restaurante existente.
     * PUT /api/v1/restaurants/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante")
    public ResponseEntity<RestaurantResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDTO requestDTO) {
        RestaurantResponseDTO responseDTO = restaurantService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deleta um restaurante.
     * DELETE /api/v1/restaurants/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar restaurante")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
