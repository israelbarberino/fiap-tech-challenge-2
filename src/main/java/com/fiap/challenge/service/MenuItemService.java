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
 * Fornece operações CRUD com validação e mapeamento de DTOs.
 */
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

    /**
     * Cria um novo item de cardápio.
     *
     * @param requestDTO dados do item a ser criado
     * @return DTO com dados do item criado
     * @throws ResourceNotFoundException se o restaurante não for encontrado
     */
    public MenuItemResponseDTO create(MenuItemRequestDTO requestDTO) {
        Restaurant restaurant = findRestaurantById(requestDTO.getRestaurantId());
        
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
     *
     * @param id ID do item
     * @return DTO com dados do item
     * @throws ResourceNotFoundException se o item não for encontrado
     */
    @Transactional(readOnly = true)
    public MenuItemResponseDTO getById(Long id) {
        MenuItem menuItem = findMenuItemById(id);
        return mapToResponseDTO(menuItem);
    }

    /**
     * Lista todos os itens de cardápio.
     *
     * @return lista de DTOs com todos os itens
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getAll() {
        return menuItemRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista itens de cardápio de um restaurante específico.
     *
     * @param restaurantId ID do restaurante
     * @return lista de DTOs com itens do restaurante
     * @throws ResourceNotFoundException se o restaurante não for encontrado
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> getByRestaurantId(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        
        return menuItemRepository.findByRestaurant(restaurant).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um item de cardápio existente.
     *
     * @param id ID do item a ser atualizado
     * @param requestDTO dados atualizados
     * @return DTO com dados do item atualizado
     * @throws ResourceNotFoundException se o item ou restaurante não forem encontrados
     */
    public MenuItemResponseDTO update(Long id, MenuItemRequestDTO requestDTO) {
        MenuItem menuItem = findMenuItemById(id);
        Restaurant restaurant = findRestaurantById(requestDTO.getRestaurantId());

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
     *
     * @param id ID do item a ser deletado
     * @throws ResourceNotFoundException se o item não for encontrado
     */
    public void delete(Long id) {
        MenuItem menuItem = findMenuItemById(id);
        menuItemRepository.delete(menuItem);
    }

    /**
     * Encontra um item de cardápio pelo ID com tratamento de erro.
     *
     * @param id ID do item
     * @return item encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    private MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENU_ITEM_NOT_FOUND_MSG + id));
    }

    /**
     * Encontra um restaurante pelo ID com tratamento de erro.
     *
     * @param restaurantId ID do restaurante
     * @return restaurante encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_MSG + restaurantId));
    }

    /**
     * Mapeia entidade MenuItem para MenuItemResponseDTO.
     *
     * @param menuItem entidade a ser mapeada
     * @return DTO com dados da entidade
     */
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
