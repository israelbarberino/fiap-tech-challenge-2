package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public class UserTypeRequestDTO {
    
    @NotBlank(message = "Nome do tipo de usuário é obrigatório")
    private String name;
    
    private String description;

    public UserTypeRequestDTO() {}

    public UserTypeRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
