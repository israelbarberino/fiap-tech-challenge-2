package com.fiap.challenge.service;

import com.fiap.challenge.dto.MenuItemRequestDTO;
import com.fiap.challenge.dto.MenuItemResponseDTO;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.MenuItem;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.MenuItemRepository;
import com.fiap.challenge.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - MenuItemService")
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem menuItem;
    private Restaurant restaurant;
    private MenuItemRequestDTO requestDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        User owner = new User("João Silva", "joao@example.com", "joao.silva", "hashedPassword", 
                              UserRole.RESTAURANT_OWNER, new Address());
        owner.setId(1L);

        Address address = new Address("Rua A", "123", "Apt 01", "São Paulo", "SP", "01000-000");
        
        restaurant = new Restaurant("Restaurante A", "Italiana", LocalTime.of(11, 0), 
                                   LocalTime.of(22, 0), address, owner);
        restaurant.setId(1L);

        menuItem = new MenuItem("Pasta Carbonara", "Pasta com molho de queijo e bacon", 
                               BigDecimal.valueOf(45.50), true, "/fotos/pasta.jpg", restaurant);
        menuItem.setId(1L);
        menuItem.setCreatedAt(now);
        menuItem.setLastModifiedAt(now);

        requestDTO = new MenuItemRequestDTO("Pasta Carbonara", "Pasta com molho de queijo e bacon", 
                                           BigDecimal.valueOf(45.50), true, "/fotos/pasta.jpg", 1L);
    }

    @Test
    @DisplayName("Deve criar um item de cardápio com sucesso")
    void testCreateMenuItem() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItemResponseDTO responseDTO = menuItemService.create(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Pasta Carbonara", responseDTO.getName());
        assertEquals(BigDecimal.valueOf(45.50), responseDTO.getPrice());
        verify(restaurantRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar item com restaurante inexistente")
    void testCreateMenuItemWithInvalidRestaurant() {
        MenuItemRequestDTO invalidRequestDTO = new MenuItemRequestDTO("Pasta Carbonara", "Pasta com molho de queijo e bacon", 
                BigDecimal.valueOf(45.50), true, "/fotos/pasta.jpg", 999L);
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.create(invalidRequestDTO));
        verify(restaurantRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar item de cardápio por ID com sucesso")
    void testGetMenuItemById() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItemResponseDTO responseDTO = menuItemService.getById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Pasta Carbonara", responseDTO.getName());
        verify(menuItemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não encontrado por ID")
    void testGetMenuItemByIdNotFound() {
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getById(999L));
        verify(menuItemRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve listar todos os itens de cardápio")
    void testGetAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(menuItem);
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        List<MenuItemResponseDTO> responseDTOs = menuItemService.getAll();

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals("Pasta Carbonara", responseDTOs.get(0).getName());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar itens de cardápio por restaurante")
    void testGetMenuItemsByRestaurant() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(menuItem);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurant(restaurant)).thenReturn(menuItems);

        List<MenuItemResponseDTO> responseDTOs = menuItemService.getByRestaurantId(1L);

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(restaurantRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).findByRestaurant(restaurant);
    }

    @Test
    @DisplayName("Deve atualizar um item de cardápio com sucesso")
    void testUpdateMenuItem() {
        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO("Pasta Atualizada", "Descrição atualizada", 
                                                              BigDecimal.valueOf(50.00), true, "/fotos/pasta2.jpg", 1L);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItemResponseDTO responseDTO = menuItemService.update(1L, updateDTO);

        assertNotNull(responseDTO);
        verify(menuItemRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Deve deletar um item de cardápio com sucesso")
    void testDeleteMenuItem() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        menuItemService.delete(1L);

        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).delete(menuItem);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar item inexistente")
    void testDeleteMenuItemNotFound() {
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.delete(999L));
        verify(menuItemRepository, times(1)).findById(999L);
    }
}
