package com.fiap.challenge.service;

import com.fiap.challenge.dto.RestaurantRequestDTO;
import com.fiap.challenge.dto.RestaurantResponseDTO;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.RestaurantRepository;
import com.fiap.challenge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar restaurantes.
 * Fornece operações CRUD para restaurantes.
 */
@Service
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo restaurante.
     */
    public RestaurantResponseDTO create(RestaurantRequestDTO requestDTO) {
        User owner = userRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.getOwnerId()));

        Restaurant restaurant = new Restaurant(
                requestDTO.getName(),
                requestDTO.getCuisineType(),
                requestDTO.getOpeningTime(),
                requestDTO.getClosingTime(),
                requestDTO.getAddress(),
                owner
        );

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponseDTO(savedRestaurant);
    }

    /**
     * Busca um restaurante por ID.
     */
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return mapToResponseDTO(restaurant);
    }

    /**
     * Busca um restaurante por nome.
     */
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getByName(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with name: " + name));
        return mapToResponseDTO(restaurant);
    }

    /**
     * Lista todos os restaurantes.
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAll() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista restaurantes por tipo de cozinha.
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um restaurante existente.
     */
    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        User owner = userRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDTO.getOwnerId()));

        restaurant.setName(requestDTO.getName());
        restaurant.setCuisineType(requestDTO.getCuisineType());
        restaurant.setOpeningTime(requestDTO.getOpeningTime());
        restaurant.setClosingTime(requestDTO.getClosingTime());
        restaurant.setAddress(requestDTO.getAddress());
        restaurant.setOwner(owner);

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponseDTO(updatedRestaurant);
    }

    /**
     * Deleta um restaurante.
     */
    public void delete(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        restaurantRepository.delete(restaurant);
    }

    private RestaurantResponseDTO mapToResponseDTO(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.getAddress(),
                restaurant.getOwner().getId(),
                restaurant.getOwner().getName(),
                restaurant.getCreatedAt(),
                restaurant.getLastModifiedAt()
        );
    }
}
