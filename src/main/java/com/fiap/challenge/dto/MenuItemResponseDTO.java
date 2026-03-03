package com.fiap.challenge.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuItemResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean availableOnPremises;
    private String photoPath;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public MenuItemResponseDTO() {}

    public MenuItemResponseDTO(Long id, String name, String description, BigDecimal price,
                              Boolean availableOnPremises, String photoPath, Long restaurantId,
                              String restaurantName, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableOnPremises = availableOnPremises;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
