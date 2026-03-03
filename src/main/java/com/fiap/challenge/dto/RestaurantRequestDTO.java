package com.fiap.challenge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fiap.challenge.entity.Address;

import java.time.LocalTime;

public class RestaurantRequestDTO {
    
    @NotBlank(message = "Nome do restaurante é obrigatório")
    private String name;
    
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    private String cuisineType;
    
    @NotNull(message = "Horário de abertura é obrigatório")
    private LocalTime openingTime;
    
    @NotNull(message = "Horário de fechamento é obrigatório")
    private LocalTime closingTime;
    
    @Valid
    private Address address;
    
    @NotNull(message = "ID do proprietário é obrigatório")
    private Long ownerId;

    public RestaurantRequestDTO() {}

    public RestaurantRequestDTO(String name, String cuisineType, LocalTime openingTime,
                               LocalTime closingTime, Address address, Long ownerId) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.address = address;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
