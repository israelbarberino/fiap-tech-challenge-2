package com.fiap.challenge.infrastructure.persistence.restaurant;

import com.fiap.challenge.domain.restaurant.model.RestaurantAggregate;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.RestaurantRepository;
import com.fiap.challenge.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantJpaGateway implements RestaurantGateway {

    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantJpaGateway(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RestaurantAggregate save(RestaurantAggregate aggregate) {
        User owner = userRepository.findById(aggregate.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG + aggregate.ownerId()));

        Restaurant entity = new Restaurant(
                aggregate.name(),
                aggregate.cuisineType(),
                aggregate.openingTime(),
                aggregate.closingTime(),
                aggregate.address(),
                owner
        );
        entity.setId(aggregate.id());
        entity.setCreatedAt(aggregate.createdAt());
        entity.setLastModifiedAt(aggregate.lastModifiedAt());

        return toAggregate(restaurantRepository.save(entity));
    }

    @Override
    public Optional<RestaurantAggregate> findById(Long id) {
        return restaurantRepository.findById(id).map(this::toAggregate);
    }

    @Override
    public Optional<RestaurantAggregate> findByName(String name) {
        return restaurantRepository.findByName(name).map(this::toAggregate);
    }

    @Override
    public List<RestaurantAggregate> findAll() {
        return restaurantRepository.findAll().stream().map(this::toAggregate).toList();
    }

    @Override
    public List<RestaurantAggregate> findByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType).stream().map(this::toAggregate).toList();
    }

    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    private RestaurantAggregate toAggregate(Restaurant entity) {
        return new RestaurantAggregate(
                entity.getId(),
                entity.getName(),
                entity.getCuisineType(),
                entity.getOpeningTime(),
                entity.getClosingTime(),
                entity.getAddress(),
                entity.getOwner().getId(),
                entity.getOwner().getName(),
                entity.getCreatedAt(),
                entity.getLastModifiedAt()
        );
    }
}
