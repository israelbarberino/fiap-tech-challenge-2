package com.fiap.challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidade que representa um restaurante no sistema.
 * Cada restaurante deve estar associado a um usuário (dono do restaurante).
 */
@Entity
@Table(name = "restaurants", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name", name = "uk_restaurants_name"),
    @UniqueConstraint(columnNames = "owner_id", name = "uk_restaurants_owner_id")
})
public class Restaurant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do restaurante é obrigatório")
    @Column(nullable = false, unique = true, length = 150)
    private String name;
    
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    @Column(nullable = false, length = 100)
    private String cuisineType;
    
    @NotNull(message = "Horário de abertura é obrigatório")
    @Column(nullable = false)
    private LocalTime openingTime;
    
    @NotNull(message = "Horário de fechamento é obrigatório")
    @Column(nullable = false)
    private LocalTime closingTime;
    
    @Valid
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "number", column = @Column(name = "address_number")),
        @AttributeOverride(name = "complement", column = @Column(name = "address_complement")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "address_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip_code"))
    })
    private Address address;
    
    @NotNull(message = "Proprietário do restaurante é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private User owner;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    public Restaurant() {}

    public Restaurant(String name, String cuisineType, LocalTime openingTime, 
                     LocalTime closingTime, Address address, User owner) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.address = address;
        this.owner = owner;
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

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
