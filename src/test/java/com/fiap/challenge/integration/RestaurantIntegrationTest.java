package com.fiap.challenge.integration;

import com.fiap.challenge.dto.RestaurantRequestDTO;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.repository.RestaurantRepository;
import com.fiap.challenge.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração - Restaurant")
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User owner;
    private RestaurantRequestDTO requestDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();

        address = new Address("Rua A", "123", "Apt 01", "São Paulo", "SP", "01000-000");
        owner = new User("João Silva", "joao@example.com", "joao.silva", 
                        passwordEncoder.encode("Senha@123"), UserRole.RESTAURANT_OWNER, address);
        owner = userRepository.save(owner);

        requestDTO = new RestaurantRequestDTO("Restaurante A", "Italiana", LocalTime.of(11, 0),
                                             LocalTime.of(22, 0), address, owner.getId());
    }

    @Test
    @DisplayName("Deve criar um restaurante via POST")
    void testCreateRestaurantEndpoint() throws Exception {
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Restaurante A")))
                .andExpect(jsonPath("$.cuisineType", is("Italiana")));
    }

    @Test
    @DisplayName("Deve buscar todos os restaurantes")
    void testGetAllRestaurantsEndpoint() throws Exception {
        User owner1 = new User("João Silva", "joao1@example.com", "joao1.silva", 
                        passwordEncoder.encode("Senha@123"), UserRole.RESTAURANT_OWNER, address);
        owner1 = userRepository.save(owner1);
        
        User owner2 = new User("Maria Santos", "maria@example.com", "maria.santos", 
                        passwordEncoder.encode("Senha@123"), UserRole.RESTAURANT_OWNER, address);
        owner2 = userRepository.save(owner2);
        
        var restaurant1 = new com.fiap.challenge.entity.Restaurant("Rest 1", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner1);
        var restaurant2 = new com.fiap.challenge.entity.Restaurant("Rest 2", "Japonesa", 
                LocalTime.of(12, 0), LocalTime.of(23, 0), address, owner2);
        
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        mockMvc.perform(get("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void testGetRestaurantByIdEndpoint() throws Exception {
        var restaurant = new com.fiap.challenge.entity.Restaurant("Restaurante A", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner);
        restaurant = restaurantRepository.save(restaurant);

        mockMvc.perform(get("/api/v1/restaurants/" + restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Restaurante A")));
    }

    @Test
    @DisplayName("Deve buscar restaurantes por tipo de cozinha")
    void testGetRestaurantsByCuisineTypeEndpoint() throws Exception {
        var restaurant = new com.fiap.challenge.entity.Restaurant("Restaurante A", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner);
        restaurantRepository.save(restaurant);

        mockMvc.perform(get("/api/v1/restaurants/cuisine/Italiana")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cuisineType", is("Italiana")));
    }

    @Test
    @DisplayName("Deve atualizar um restaurante")
    void testUpdateRestaurantEndpoint() throws Exception {
        var restaurant = new com.fiap.challenge.entity.Restaurant("Restaurante A", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner);
        restaurant = restaurantRepository.save(restaurant);

        RestaurantRequestDTO updateDTO = new RestaurantRequestDTO("Restaurante Atualizado", "Francesa",
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner.getId());
        String requestBody = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/v1/restaurants/" + restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Restaurante Atualizado")))
                .andExpect(jsonPath("$.cuisineType", is("Francesa")));
    }

    @Test
    @DisplayName("Deve deletar um restaurante")
    void testDeleteRestaurantEndpoint() throws Exception {
        var restaurant = new com.fiap.challenge.entity.Restaurant("Restaurante A", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner);
        restaurant = restaurantRepository.save(restaurant);

        mockMvc.perform(delete("/api/v1/restaurants/" + restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/restaurants/" + restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar restaurante inexistente")
    void testGetRestaurantNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
