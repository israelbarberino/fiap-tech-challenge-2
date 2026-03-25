package com.fiap.challenge.controller;

import com.fiap.challenge.application.restaurant.dto.CreateRestaurantCommand;
import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.dto.UpdateRestaurantCommand;
import com.fiap.challenge.application.restaurant.usecase.CreateRestaurantUseCase;
import com.fiap.challenge.application.restaurant.usecase.DeleteRestaurantUseCase;
import com.fiap.challenge.application.restaurant.usecase.GetAllRestaurantsUseCase;
import com.fiap.challenge.application.restaurant.usecase.GetRestaurantByIdUseCase;
import com.fiap.challenge.application.restaurant.usecase.GetRestaurantByNameUseCase;
import com.fiap.challenge.application.restaurant.usecase.GetRestaurantsByCuisineTypeUseCase;
import com.fiap.challenge.application.restaurant.usecase.UpdateRestaurantUseCase;
import com.fiap.challenge.dto.RestaurantRequestDTO;
import com.fiap.challenge.dto.RestaurantResponseDTO;
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

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final GetRestaurantByNameUseCase getRestaurantByNameUseCase;
    private final GetAllRestaurantsUseCase getAllRestaurantsUseCase;
    private final GetRestaurantsByCuisineTypeUseCase getRestaurantsByCuisineTypeUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            GetRestaurantByIdUseCase getRestaurantByIdUseCase,
            GetRestaurantByNameUseCase getRestaurantByNameUseCase,
            GetAllRestaurantsUseCase getAllRestaurantsUseCase,
            GetRestaurantsByCuisineTypeUseCase getRestaurantsByCuisineTypeUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.getRestaurantByIdUseCase = getRestaurantByIdUseCase;
        this.getRestaurantByNameUseCase = getRestaurantByNameUseCase;
        this.getAllRestaurantsUseCase = getAllRestaurantsUseCase;
        this.getRestaurantsByCuisineTypeUseCase = getRestaurantsByCuisineTypeUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
    }

    /**
     * Cria um novo restaurante.
     * POST /api/v1/restaurants
     */
    @PostMapping
    @Operation(summary = "Criar novo restaurante")
    public ResponseEntity<RestaurantResponseDTO> create(@Valid @RequestBody RestaurantRequestDTO requestDTO) {
        RestaurantResult result = createRestaurantUseCase.execute(new CreateRestaurantCommand(
                requestDTO.name(),
                requestDTO.cuisineType(),
                requestDTO.openingTime(),
                requestDTO.closingTime(),
                requestDTO.address(),
                requestDTO.ownerId()
        ));
        RestaurantResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Busca um restaurante por ID.
     * GET /api/v1/restaurants/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID")
    public ResponseEntity<RestaurantResponseDTO> getById(@PathVariable Long id) {
        RestaurantResponseDTO responseDTO = toResponseDTO(getRestaurantByIdUseCase.execute(id));
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um restaurante por nome.
     * GET /api/v1/restaurants/name/{name}
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar restaurante por nome")
    public ResponseEntity<RestaurantResponseDTO> getByName(@PathVariable String name) {
        RestaurantResponseDTO responseDTO = toResponseDTO(getRestaurantByNameUseCase.execute(name));
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os restaurantes.
     * GET /api/v1/restaurants
     */
    @GetMapping
    @Operation(summary = "Listar todos os restaurantes")
    public ResponseEntity<List<RestaurantResponseDTO>> getAll() {
        List<RestaurantResponseDTO> responseDTOs = getAllRestaurantsUseCase.execute().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Lista restaurantes por tipo de cozinha.
     * GET /api/v1/restaurants/cuisine/{cuisineType}
     */
    @GetMapping("/cuisine/{cuisineType}")
    @Operation(summary = "Listar restaurantes por tipo de cozinha")
    public ResponseEntity<List<RestaurantResponseDTO>> getByCuisineType(@PathVariable String cuisineType) {
        List<RestaurantResponseDTO> responseDTOs = getRestaurantsByCuisineTypeUseCase.execute(cuisineType).stream()
                .map(this::toResponseDTO)
                .toList();
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
        RestaurantResult result = updateRestaurantUseCase.execute(id, new UpdateRestaurantCommand(
            requestDTO.name(),
            requestDTO.cuisineType(),
            requestDTO.openingTime(),
            requestDTO.closingTime(),
            requestDTO.address(),
            requestDTO.ownerId()
        ));
        RestaurantResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deleta um restaurante.
     * DELETE /api/v1/restaurants/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar restaurante")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private RestaurantResponseDTO toResponseDTO(RestaurantResult result) {
        return new RestaurantResponseDTO(
                result.id(),
                result.name(),
                result.cuisineType(),
                result.openingTime(),
                result.closingTime(),
                result.address(),
                result.ownerId(),
                result.ownerName(),
                result.createdAt(),
                result.lastModifiedAt()
        );
    }
}
