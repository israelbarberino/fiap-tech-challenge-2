package com.fiap.challenge.dto;

import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    String login,
    UserRole role,
    AddressDTO address,
    LocalDateTime createdAt,
    LocalDateTime lastModifiedAt
) {
    public UserResponse(User user) {
        this(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getLogin(),
            user.getRole(),
            buildAddress(user),
            user.getCreatedAt(),
            user.getLastModifiedAt()
        );
    }

    private static AddressDTO buildAddress(User user) {
        Address addr = user.getAddress();
        if (addr == null) {
            return null;
        }
        return new AddressDTO(
            addr.getStreet(),
            addr.getNumber(),
            addr.getComplement(),
            addr.getCity(),
            addr.getState(),
            addr.getZipCode()
        );
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getLogin() { return login; }
    public UserRole getRole() { return role; }
    public AddressDTO getAddress() { return address; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastModifiedAt() { return lastModifiedAt; }
}
