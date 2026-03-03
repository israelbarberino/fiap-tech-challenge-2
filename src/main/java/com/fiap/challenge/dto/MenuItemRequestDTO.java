package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class MenuItemRequestDTO {
    
    @NotBlank(message = "Nome do item é obrigatório")
    private String name;
    
    private String description;
    
    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço não pode ser negativo")
    private BigDecimal price;
    
    @NotNull(message = "Disponibilidade no restaurante é obrigatória")
    private Boolean availableOnPremises;
    
    private String photoPath;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    private Long restaurantId;

    public MenuItemRequestDTO() {}

    public MenuItemRequestDTO(String name, String description, BigDecimal price,
                             Boolean availableOnPremises, String photoPath, Long restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableOnPremises = availableOnPremises;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailableOnPremises() {
        return availableOnPremises;
    }

    public void setAvailableOnPremises(Boolean availableOnPremises) {
        this.availableOnPremises = availableOnPremises;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
