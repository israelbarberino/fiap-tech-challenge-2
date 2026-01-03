package com.fiap.challenge.controller;

import com.fiap.challenge.dto.*;
import com.fiap.challenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content =
    @Content(mediaType = "application/json", schema =
    @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "409", description = "Email ou login já registrado")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário", description = "Obtém um usuário específico pelo ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content =
    @Content(mediaType = "application/json", schema =
    @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Buscar usuários", description = "Busca usuários pelo nome")
    @ApiResponse(responseCode = "200", description = "Usuários encontrados", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<UserResponse>> getUsersByName(
            @Parameter(description = "Nome para buscar") @RequestParam String name) {
        List<UserResponse> response = userService.getUsersByName(name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente (não altera senha)")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content =
    @Content(mediaType = "application/json", schema =
    @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "409", description = "Email já registrado")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário")
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "401", description = "Senha atual incorreta")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou senhas não conferem")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário do sistema")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
