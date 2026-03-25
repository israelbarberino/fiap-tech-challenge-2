package com.fiap.challenge.application.usertype.dto;

import java.time.LocalDateTime;

public record UserTypeResult(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {
}
