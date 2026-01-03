package com.fiap.challenge.controller;

import com.fiap.challenge.dto.LoginValidateRequest;
import com.fiap.challenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e validação de credenciais")
public class AuthController {
    
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar credenciais", description = "Valida as credenciais de login do usuário")
    @ApiResponse(responseCode = "200", description = "Credenciais válidas", content =
    @Content(mediaType = "application/json", schema =
    @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public ResponseEntity<Map<String, String>> validateLogin(
            @Valid @RequestBody LoginValidateRequest request) {
        userService.validateLogin(request);
        return ResponseEntity.ok(Map.of("message", "Credenciais válidas"));
    }
}
