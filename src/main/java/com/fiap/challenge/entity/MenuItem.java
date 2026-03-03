package com.fiap.challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um item do cardápio de um restaurante.
 * Cada item está associado a um restaurante específico.
 */
@Entity
@Table(name = "menu_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "uk_menu_items_restaurant_name")
})
public class MenuItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do item é obrigatório")
    @Column(nullable = false, length = 150)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço não pode ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "Disponibilidade no restaurante é obrigatória")
    @Column(name = "available_on_premises", nullable = false)
    private Boolean availableOnPremises;
    
    @Column(length = 500, name = "photo_path")
    private String photoPath;
    
    @NotNull(message = "Restaurante é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    public MenuItem() {}

    public MenuItem(String name, String description, BigDecimal price, 
                   Boolean availableOnPremises, String photoPath, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableOnPremises = availableOnPremises;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", availableOnPremises=" + availableOnPremises +
                ", photoPath='" + photoPath + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
