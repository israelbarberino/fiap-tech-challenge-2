package com.fiap.challenge.dto;

import java.time.LocalDateTime;

public record UserTypeResponseDTO(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime lastModifiedAt
) {}
