package com.fiap.challenge.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEntitiesCoverageTest {

    @Test
    @DisplayName("Deve preencher e ler campos de UserType")
    void shouldPopulateUserType() {
        UserType userType = new UserType("Dono", "Descricao inicial");
        userType.setId(1L);
        userType.setName("Cliente");
        userType.setDescription("Descricao final");

        LocalDateTime now = LocalDateTime.now();
        userType.setCreatedAt(now);
        userType.setLastModifiedAt(now);

        assertEquals(1L, userType.getId());
        assertEquals("Cliente", userType.getName());
        assertEquals("Descricao final", userType.getDescription());
        assertEquals(now, userType.getCreatedAt());
        assertEquals(now, userType.getLastModifiedAt());
        assertTrue(userType.toString().contains("Cliente"));
    }

    @Test
    @DisplayName("Deve preencher e ler campos de Restaurant")
    void shouldPopulateRestaurant() {
        Address address = new Address("Rua A", "10", "Apto 1", "Sao Paulo", "SP", "01000-000");
        User owner = new User("Owner", "owner@example.com", "owner.login", "hash", UserRole.RESTAURANT_OWNER, address);
        owner.setId(7L);

        Restaurant restaurant = new Restaurant(
                "Restaurante Teste",
                "Brasileira",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                address,
                owner
        );

        restaurant.setId(11L);
        restaurant.setName("Restaurante Atualizado");
        restaurant.setCuisineType("Italiana");
        restaurant.setOpeningTime(LocalTime.of(11, 0));
        restaurant.setClosingTime(LocalTime.of(23, 0));

        LocalDateTime now = LocalDateTime.now();
        restaurant.setCreatedAt(now);
        restaurant.setLastModifiedAt(now);

        assertEquals(11L, restaurant.getId());
        assertEquals("Restaurante Atualizado", restaurant.getName());
        assertEquals("Italiana", restaurant.getCuisineType());
        assertEquals(LocalTime.of(11, 0), restaurant.getOpeningTime());
        assertEquals(LocalTime.of(23, 0), restaurant.getClosingTime());
        assertEquals(address, restaurant.getAddress());
        assertEquals(owner, restaurant.getOwner());
        assertEquals(now, restaurant.getCreatedAt());
        assertEquals(now, restaurant.getLastModifiedAt());
        assertTrue(restaurant.toString().contains("Restaurante Atualizado"));
    }

    @Test
    @DisplayName("Deve preencher e ler campos de MenuItem")
    void shouldPopulateMenuItem() {
        Address address = new Address("Rua B", "20", "Casa", "Santos", "SP", "11000-000");
        User owner = new User("Owner 2", "owner2@example.com", "owner2.login", "hash", UserRole.RESTAURANT_OWNER, address);
        owner.setId(8L);
        Restaurant restaurant = new Restaurant("Rest", "Japonesa", LocalTime.of(9, 0), LocalTime.of(21, 0), address, owner);
        restaurant.setId(21L);

        MenuItem menuItem = new MenuItem(
                "Sushi",
                "Combo",
                BigDecimal.valueOf(59.90),
                true,
                "/images/sushi.jpg",
                restaurant
        );

        menuItem.setId(31L);
        menuItem.setName("Sushi Especial");
        menuItem.setDescription("Combo premium");
        menuItem.setPrice(BigDecimal.valueOf(79.90));
        menuItem.setAvailableOnPremises(false);
        menuItem.setPhotoPath("/images/sushi-especial.jpg");

        LocalDateTime now = LocalDateTime.now();
        menuItem.setCreatedAt(now);
        menuItem.setLastModifiedAt(now);

        assertEquals(31L, menuItem.getId());
        assertEquals("Sushi Especial", menuItem.getName());
        assertEquals("Combo premium", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(79.90), menuItem.getPrice());
        assertEquals(false, menuItem.getAvailableOnPremises());
        assertEquals("/images/sushi-especial.jpg", menuItem.getPhotoPath());
        assertEquals(restaurant, menuItem.getRestaurant());
        assertEquals(now, menuItem.getCreatedAt());
        assertEquals(now, menuItem.getLastModifiedAt());
        assertTrue(menuItem.toString().contains("Sushi Especial"));
    }
}
