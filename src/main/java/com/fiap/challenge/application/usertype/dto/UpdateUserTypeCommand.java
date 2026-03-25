package com.fiap.challenge.application.usertype.dto;

public record UpdateUserTypeCommand(
        String name,
        String description
) {
}
