package com.fiap.challenge.service;

import com.fiap.challenge.dto.MenuItemRequestDTO;
import com.fiap.challenge.dto.MenuItemResponseDTO;
import com.fiap.challenge.entity.MenuItem;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.MenuItemRepository;
import com.fiap.challenge.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar itens do cardápio.
 * Fornece operações CRUD para itens do cardápio.
 */
@Service
@Transactional
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Cria um novo item de cardápio.
     */
    public MenuItemResponseDTO create(MenuItemRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.getRestaurantId()));

        MenuItem menuItem = new MenuItem(
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getPrice(),
                requestDTO.getAvailableOnPremises(),
                requestDTO.getPhotoPath(),
                restaurant
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponseDTO(savedMenuItem);
    }

    /**
     * Busca um item de cardápio por ID.
     */
    @Transactional(readOnly = true)
    public MenuItemResponseDTO getById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
        return mapToResponseDTO(menuItem);
    }

    /**
     * Lista todos os itens de cardápio.
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getAll() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista itens de cardápio de um restaurante específico.
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        return menuItemRepository.findByRestaurant(restaurant).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um item de cardápio existente.
     */
    public MenuItemResponseDTO update(Long id, MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));

        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.getRestaurantId()));

        menuItem.setName(requestDTO.getName());
        menuItem.setDescription(requestDTO.getDescription());
        menuItem.setPrice(requestDTO.getPrice());
        menuItem.setAvailableOnPremises(requestDTO.getAvailableOnPremises());
        menuItem.setPhotoPath(requestDTO.getPhotoPath());
        menuItem.setRestaurant(restaurant);

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponseDTO(updatedMenuItem);
    }

    /**
     * Deleta um item de cardápio.
     */
    public void delete(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
        menuItemRepository.delete(menuItem);
    }

    private MenuItemResponseDTO mapToResponseDTO(MenuItem menuItem) {
        return new MenuItemResponseDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getAvailableOnPremises(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurant().getId(),
                menuItem.getRestaurant().getName(),
                menuItem.getCreatedAt(),
                menuItem.getLastModifiedAt()
        );
    }
}
