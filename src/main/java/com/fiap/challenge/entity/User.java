package com.fiap.challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email", name = "uk_users_email"),
    @UniqueConstraint(columnNames = "login", name = "uk_users_login")})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 150)
    private String name;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", 
            message = "Email inválido: deve estar no formato correto (ex: usuario@exemplo.com)")
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    
    @NotBlank(message = "Login é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String login;
    
    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false, length = 255)
    private String passwordHash;
    
    @NotNull(message = "Papel do usuário é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;
    
    @Valid
    @Embedded
    private Address address;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    public User() {}

    public User(String name, String email, String login, String passwordHash, UserRole role, Address address) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastModifiedAt() { return lastModifiedAt; }
    public void setLastModifiedAt(LocalDateTime lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
