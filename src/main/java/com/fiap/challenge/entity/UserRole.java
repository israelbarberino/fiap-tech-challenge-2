package com.fiap.challenge.entity;

public enum UserRole {

    RESTAURANT_OWNER("Proprietário de Restaurante"),
    CUSTOMER("Cliente");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
