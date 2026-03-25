package com.fiap.challenge.domain.usertype.model;

import java.time.LocalDateTime;

public record UserTypeAggregate(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {
}
