package com.fiap.challenge.controller;

import com.fiap.challenge.application.usertype.dto.CreateUserTypeCommand;
import com.fiap.challenge.application.usertype.dto.UpdateUserTypeCommand;
import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.application.usertype.usecase.CreateUserTypeUseCase;
import com.fiap.challenge.application.usertype.usecase.DeleteUserTypeUseCase;
import com.fiap.challenge.application.usertype.usecase.GetAllUserTypesUseCase;
import com.fiap.challenge.application.usertype.usecase.GetUserTypeByIdUseCase;
import com.fiap.challenge.application.usertype.usecase.GetUserTypeByNameUseCase;
import com.fiap.challenge.application.usertype.usecase.UpdateUserTypeUseCase;
import com.fiap.challenge.dto.UserTypeRequestDTO;
import com.fiap.challenge.dto.UserTypeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar tipos de usuários.
 * Fornece endpoints REST para operações CRUD em tipos de usuários.
 */
@RestController
@RequestMapping("/api/v1/user-types")
@Tag(name = "User Types", description = "Endpoints para gerenciar tipos de usuários")
public class UserTypeController {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final GetUserTypeByIdUseCase getUserTypeByIdUseCase;
    private final GetUserTypeByNameUseCase getUserTypeByNameUseCase;
    private final GetAllUserTypesUseCase getAllUserTypesUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;

    public UserTypeController(
            CreateUserTypeUseCase createUserTypeUseCase,
            GetUserTypeByIdUseCase getUserTypeByIdUseCase,
            GetUserTypeByNameUseCase getUserTypeByNameUseCase,
            GetAllUserTypesUseCase getAllUserTypesUseCase,
            UpdateUserTypeUseCase updateUserTypeUseCase,
            DeleteUserTypeUseCase deleteUserTypeUseCase) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.getUserTypeByIdUseCase = getUserTypeByIdUseCase;
        this.getUserTypeByNameUseCase = getUserTypeByNameUseCase;
        this.getAllUserTypesUseCase = getAllUserTypesUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
    }

    /**
     * Cria um novo tipo de usuário.
     * POST /api/v1/user-types
     */
    @PostMapping
    @Operation(summary = "Criar novo tipo de usuário")
    public ResponseEntity<UserTypeResponseDTO> create(@Valid @RequestBody UserTypeRequestDTO requestDTO) {
        UserTypeResult result = createUserTypeUseCase.execute(
                new CreateUserTypeCommand(requestDTO.name(), requestDTO.description())
        );
        UserTypeResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Busca um tipo de usuário por ID.
     * GET /api/v1/user-types/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário por ID")
    public ResponseEntity<UserTypeResponseDTO> getById(@PathVariable Long id) {
        UserTypeResponseDTO responseDTO = toResponseDTO(getUserTypeByIdUseCase.execute(id));
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um tipo de usuário por nome.
     * GET /api/v1/user-types/name/{name}
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar tipo de usuário por nome")
    public ResponseEntity<UserTypeResponseDTO> getByName(@PathVariable String name) {
        UserTypeResponseDTO responseDTO = toResponseDTO(getUserTypeByNameUseCase.execute(name));
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os tipos de usuários.
     * GET /api/v1/user-types
     */
    @GetMapping
    @Operation(summary = "Listar todos os tipos de usuários")
    public ResponseEntity<List<UserTypeResponseDTO>> getAll() {
        List<UserTypeResponseDTO> responseDTOs = getAllUserTypesUseCase.execute().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Atualiza um tipo de usuário existente.
     * PUT /api/v1/user-types/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de usuário")
    public ResponseEntity<UserTypeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserTypeRequestDTO requestDTO) {
        UserTypeResult result = updateUserTypeUseCase.execute(
            id,
            new UpdateUserTypeCommand(requestDTO.name(), requestDTO.description())
        );
        UserTypeResponseDTO responseDTO = toResponseDTO(result);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deleta um tipo de usuário.
     * DELETE /api/v1/user-types/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tipo de usuário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserTypeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private UserTypeResponseDTO toResponseDTO(UserTypeResult result) {
        return new UserTypeResponseDTO(
                result.id(),
                result.name(),
                result.description(),
                result.createdAt(),
                result.lastModifiedAt()
        );
    }
}
