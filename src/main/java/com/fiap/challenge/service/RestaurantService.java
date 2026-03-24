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
 * Fornece operações CRUD com validação e mapeamento de DTOs.
 */
@Service
@Transactional
public class RestaurantService {

    private static final String RESTAURANT_NOT_FOUND_ID_MSG = "Restaurant not found with id: ";
    private static final String RESTAURANT_NOT_FOUND_NAME_MSG = "Restaurant not found with name: ";
    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo restaurante.
     *
     * @param requestDTO dados do restaurante a ser criado
     * @return DTO com dados do restaurante criado
     * @throws ResourceNotFoundException se o proprietário não for encontrado
     */
    public RestaurantResponseDTO create(RestaurantRequestDTO requestDTO) {
        User owner = findUserById(requestDTO.getOwnerId());

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
     *
     * @param id ID do restaurante
     * @return DTO com dados do restaurante
     * @throws ResourceNotFoundException se não encontrado
     */
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getById(Long id) {
        Restaurant restaurant = findRestaurantById(id);
        return mapToResponseDTO(restaurant);
    }

    /**
     * Busca um restaurante por nome.
     *
     * @param name nome do restaurante
     * @return DTO com dados do restaurante
     * @throws ResourceNotFoundException se não encontrado
     */
    @Transactional(readOnly = true)
    public RestaurantResponseDTO getByName(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_NAME_MSG + name));
        return mapToResponseDTO(restaurant);
    }

    /**
     * Lista todos os restaurantes.
     *
     * @return lista de DTOs com todos os restaurantes
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getAll() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista restaurantes por tipo de cozinha.
     *
     * @param cuisineType tipo de cozinha
     * @return lista de DTOs com restaurantes do tipo especificado
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponseDTO> getByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um restaurante existente.
     *
     * @param id ID do restaurante a ser atualizado
     * @param requestDTO dados atualizados
     * @return DTO com dados do restaurante atualizado
     * @throws ResourceNotFoundException se o restaurante ou proprietário não forem encontrados
     */
    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = findRestaurantById(id);
        User owner = findUserById(requestDTO.getOwnerId());

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
     *
     * @param id ID do restaurante a ser deletado
     * @throws ResourceNotFoundException se não encontrado
     */
    public void delete(Long id) {
        Restaurant restaurant = findRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    /**
     * Encontra um restaurante pelo ID com tratamento de erro.
     *
     * @param id ID do restaurante
     * @return restaurante encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_ID_MSG + id));
    }

    /**
     * Encontra um usuário pelo ID com tratamento de erro.
     *
     * @param userId ID do usuário
     * @return usuário encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG + userId));
    }

    /**
     * Mapeia entidade Restaurant para RestaurantResponseDTO.
     *
     * @param restaurant entidade a ser mapeada
     * @return DTO com dados da entidade
     */
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
