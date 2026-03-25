package com.fiap.challenge.service;

import com.fiap.challenge.dto.RestaurantRequestDTO;
import com.fiap.challenge.dto.RestaurantResponseDTO;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.RestaurantRepository;
import com.fiap.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - RestaurantService")
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private User owner;
    private RestaurantRequestDTO requestDTO;
    private Address address;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        owner = new User("João Silva", "joao@example.com", "joao.silva", "hashedPassword", 
                        UserRole.RESTAURANT_OWNER, new Address());
        owner.setId(1L);

        address = new Address("Rua A", "123", "Apt 01", "São Paulo", "SP", "01000-000");
        
        restaurant = new Restaurant("Restaurante A", "Italiana", LocalTime.of(11, 0), 
                                   LocalTime.of(22, 0), address, owner);
        restaurant.setId(1L);
        restaurant.setCreatedAt(now);
        restaurant.setLastModifiedAt(now);

        requestDTO = new RestaurantRequestDTO("Restaurante A", "Italiana", LocalTime.of(11, 0),
                                             LocalTime.of(22, 0), address, 1L);
    }

    @Test
    @DisplayName("Deve criar um restaurante com sucesso")
    void testCreateRestaurant() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantResponseDTO responseDTO = restaurantService.create(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Restaurante A", responseDTO.name());
        assertEquals("Italiana", responseDTO.cuisineType());
        verify(userRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar restaurante com proprietário inexistente")
    void testCreateRestaurantWithInvalidOwner() {
        RestaurantRequestDTO invalidRequestDTO = new RestaurantRequestDTO("Restaurante A", "Italiana", LocalTime.of(11, 0),
                LocalTime.of(22, 0), address, 999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.create(invalidRequestDTO));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void testGetRestaurantById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO responseDTO = restaurantService.getById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.id());
        assertEquals("Restaurante A", responseDTO.name());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não encontrado por ID")
    void testGetRestaurantByIdNotFound() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getById(999L));
        verify(restaurantRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar restaurante por nome com sucesso")
    void testGetRestaurantByName() {
        when(restaurantRepository.findByName("Restaurante A")).thenReturn(Optional.of(restaurant));

        RestaurantResponseDTO responseDTO = restaurantService.getByName("Restaurante A");

        assertNotNull(responseDTO);
        assertEquals("Restaurante A", responseDTO.name());
        verify(restaurantRepository, times(1)).findByName("Restaurante A");
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void testGetAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<RestaurantResponseDTO> responseDTOs = restaurantService.getAll();

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals("Restaurante A", responseDTOs.get(0).name());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar restaurantes por tipo de cozinha")
    void testGetRestaurantsByCuisineType() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        when(restaurantRepository.findByCuisineType("Italiana")).thenReturn(restaurants);

        List<RestaurantResponseDTO> responseDTOs = restaurantService.getByCuisineType("Italiana");

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals("Italiana", responseDTOs.get(0).cuisineType());
        verify(restaurantRepository, times(1)).findByCuisineType("Italiana");
    }

    @Test
    @DisplayName("Deve deletar um restaurante com sucesso")
    void testDeleteRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.delete(1L);

        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).delete(restaurant);
    }
}
