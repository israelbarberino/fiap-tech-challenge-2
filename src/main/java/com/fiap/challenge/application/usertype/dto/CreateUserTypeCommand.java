package com.fiap.challenge.application.usertype.dto;

public record CreateUserTypeCommand(
        String name,
        String description
) {
}
