package com.fiap.challenge.integration;

import com.fiap.challenge.dto.MenuItemRequestDTO;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.repository.MenuItemRepository;
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

import java.math.BigDecimal;
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
@DisplayName("Testes de Integração - MenuItem")
class MenuItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private com.fiap.challenge.entity.Restaurant restaurant;
    private MenuItemRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();

        Address address = new Address("Rua A", "123", "Apt 01", "São Paulo", "SP", "01000-000");
        User owner = new User("João Silva", "joao@example.com", "joao.silva", 
                             passwordEncoder.encode("Senha@123"), UserRole.RESTAURANT_OWNER, address);
        owner = userRepository.save(owner);

        restaurant = new com.fiap.challenge.entity.Restaurant("Restaurante A", "Italiana", 
                LocalTime.of(11, 0), LocalTime.of(22, 0), address, owner);
        restaurant = restaurantRepository.save(restaurant);

        requestDTO = new MenuItemRequestDTO("Pasta Carbonara", "Pasta com molho de queijo e bacon", 
                                           BigDecimal.valueOf(45.50), true, "/fotos/pasta.jpg", restaurant.getId());
    }

    @Test
    @DisplayName("Deve criar um item de cardápio via POST")
    void testCreateMenuItemEndpoint() throws Exception {
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Pasta Carbonara")))
                .andExpect(jsonPath("$.price", is(45.50)));
    }

    @Test
    @DisplayName("Deve buscar todos os itens de cardápio")
    void testGetAllMenuItemsEndpoint() throws Exception {
        var item1 = new com.fiap.challenge.entity.MenuItem("Pasta 1", "Descrição 1", 
                BigDecimal.valueOf(45.50), true, "/foto1.jpg", restaurant);
        var item2 = new com.fiap.challenge.entity.MenuItem("Pasta 2", "Descrição 2", 
                BigDecimal.valueOf(50.00), true, "/foto2.jpg", restaurant);
        
        menuItemRepository.save(item1);
        menuItemRepository.save(item2);

        mockMvc.perform(get("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Deve buscar item de cardápio por ID")
    void testGetMenuItemByIdEndpoint() throws Exception {
        var menuItem = new com.fiap.challenge.entity.MenuItem("Pasta Carbonara", "Descrição", 
                BigDecimal.valueOf(45.50), true, "/foto.jpg", restaurant);
        menuItem = menuItemRepository.save(menuItem);

        mockMvc.perform(get("/api/v1/menu-items/" + menuItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Pasta Carbonara")));
    }

    @Test
    @DisplayName("Deve buscar itens de cardápio por restaurante")
    void testGetMenuItemsByRestaurantEndpoint() throws Exception {
        var menuItem = new com.fiap.challenge.entity.MenuItem("Pasta Carbonara", "Descrição", 
                BigDecimal.valueOf(45.50), true, "/foto.jpg", restaurant);
        menuItemRepository.save(menuItem);

        mockMvc.perform(get("/api/v1/menu-items/restaurant/" + restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].restaurantName", is("Restaurante A")));
    }

    @Test
    @DisplayName("Deve atualizar um item de cardápio")
    void testUpdateMenuItemEndpoint() throws Exception {
        var menuItem = new com.fiap.challenge.entity.MenuItem("Pasta Carbonara", "Descrição", 
                BigDecimal.valueOf(45.50), true, "/foto.jpg", restaurant);
        menuItem = menuItemRepository.save(menuItem);

        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO("Pasta Atualizada", "Nova descrição", 
                BigDecimal.valueOf(50.00), true, "/foto2.jpg", restaurant.getId());
        String requestBody = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/api/v1/menu-items/" + menuItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Pasta Atualizada")))
                .andExpect(jsonPath("$.price", is(50.00)));
    }

    @Test
    @DisplayName("Deve deletar um item de cardápio")
    void testDeleteMenuItemEndpoint() throws Exception {
        var menuItem = new com.fiap.challenge.entity.MenuItem("Pasta Carbonara", "Descrição", 
                BigDecimal.valueOf(45.50), true, "/foto.jpg", restaurant);
        menuItem = menuItemRepository.save(menuItem);

        mockMvc.perform(delete("/api/v1/menu-items/" + menuItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/menu-items/" + menuItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar item inexistente")
    void testGetMenuItemNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/menu-items/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar erro ao criar item com dados inválidos")
    void testCreateMenuItemWithInvalidData() throws Exception {
        MenuItemRequestDTO invalidDTO = new MenuItemRequestDTO("", "Descrição", 
                BigDecimal.valueOf(45.50), true, "/foto.jpg", restaurant.getId());
        String requestBody = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/v1/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
