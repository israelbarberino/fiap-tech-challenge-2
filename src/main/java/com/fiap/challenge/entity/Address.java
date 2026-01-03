package com.fiap.challenge.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Address {
    
    @NotBlank(message = "Rua é obrigatória")
    private String street;
    
    @NotBlank(message = "Número é obrigatório")
    private String number;
    
    private String complement;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String city;
    
    @NotBlank(message = "Estado é obrigatório")
    private String state;
    
    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;

    public Address() {}

    public Address(String street, String number, String complement, String city, String state, String zipCode) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
