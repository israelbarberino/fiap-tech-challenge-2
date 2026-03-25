package com.fiap.challenge.infrastructure.persistence.menuitem;

import com.fiap.challenge.domain.menuitem.model.MenuItemAggregate;
import com.fiap.challenge.domain.menuitem.port.MenuItemGateway;
import com.fiap.challenge.entity.MenuItem;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.MenuItemRepository;
import com.fiap.challenge.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemJpaGateway implements MenuItemGateway {

    private static final String RESTAURANT_NOT_FOUND_MSG = "Restaurant not found with id: ";

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemJpaGateway(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuItemAggregate save(MenuItemAggregate aggregate) {
        Restaurant restaurant = restaurantRepository.findById(aggregate.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_MSG + aggregate.restaurantId()));

        MenuItem entity = new MenuItem(
                aggregate.name(),
                aggregate.description(),
                aggregate.price(),
                aggregate.availableOnPremises(),
                aggregate.photoPath(),
                restaurant
        );
        entity.setId(aggregate.id());
        entity.setCreatedAt(aggregate.createdAt());
        entity.setLastModifiedAt(aggregate.lastModifiedAt());

        return toAggregate(menuItemRepository.save(entity));
    }

    @Override
    public Optional<MenuItemAggregate> findById(Long id) {
        return menuItemRepository.findById(id).map(this::toAggregate);
    }

    @Override
    public List<MenuItemAggregate> findAll() {
        return menuItemRepository.findAll().stream().map(this::toAggregate).toList();
    }

    @Override
    public List<MenuItemAggregate> findByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_MSG + restaurantId));
        return menuItemRepository.findByRestaurant(restaurant).stream().map(this::toAggregate).toList();
    }

    @Override
    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemAggregate toAggregate(MenuItem entity) {
        return new MenuItemAggregate(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getAvailableOnPremises(),
                entity.getPhotoPath(),
                entity.getRestaurant().getId(),
                entity.getRestaurant().getName(),
                entity.getCreatedAt(),
                entity.getLastModifiedAt()
        );
    }
}
