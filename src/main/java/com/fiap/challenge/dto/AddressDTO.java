package com.fiap.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
    @NotBlank(message = "Rua é obrigatória")
    String street,

    @NotBlank(message = "Número é obrigatório")
    String number,

    String complement,

    @NotBlank(message = "Cidade é obrigatória")
    String city,

    @NotBlank(message = "Estado é obrigatório")
    String state,

    @NotBlank(message = "CEP é obrigatório")
    String zipCode
) {
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
}
