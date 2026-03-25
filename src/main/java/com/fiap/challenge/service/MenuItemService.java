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

@Service
@Transactional
public class MenuItemService {

    private static final String MENU_ITEM_NOT_FOUND_MSG = "MenuItem not found with id: ";
    private static final String RESTAURANT_NOT_FOUND_MSG = "Restaurant not found with id: ";

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItemResponseDTO create(MenuItemRequestDTO requestDTO) {
        Restaurant restaurant = findRestaurantById(requestDTO.restaurantId());
        
        MenuItem menuItem = new MenuItem(
                requestDTO.name(),
                requestDTO.description(),
                requestDTO.price(),
                requestDTO.availableOnPremises(),
                requestDTO.photoPath(),
                restaurant
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponseDTO(savedMenuItem);
    }

    @Transactional(readOnly = true)
    public MenuItemResponseDTO getById(Long id) {
        MenuItem menuItem = findMenuItemById(id);
        return mapToResponseDTO(menuItem);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getAll() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getByRestaurantId(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        
        return menuItemRepository.findByRestaurant(restaurant).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MenuItemResponseDTO update(Long id, MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = findMenuItemById(id);
        Restaurant restaurant = findRestaurantById(requestDTO.restaurantId());

        menuItem.setName(requestDTO.name());
        menuItem.setDescription(requestDTO.description());
        menuItem.setPrice(requestDTO.price());
        menuItem.setAvailableOnPremises(requestDTO.availableOnPremises());
        menuItem.setPhotoPath(requestDTO.photoPath());
        menuItem.setRestaurant(restaurant);

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return mapToResponseDTO(updatedMenuItem);
    }

    public void delete(Long id) {
        MenuItem menuItem = findMenuItemById(id);
        menuItemRepository.delete(menuItem);
    }

    private MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_ITEM_NOT_FOUND_MSG + id));
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_MSG + restaurantId));
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
