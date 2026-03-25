package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.dto.UpdateRestaurantCommand;
import com.fiap.challenge.application.restaurant.mapper.RestaurantApplicationMapper;
import com.fiap.challenge.domain.restaurant.model.RestaurantAggregate;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateRestaurantUseCase {

    private static final String RESTAURANT_NOT_FOUND_ID_MSG = "Restaurant not found with id: ";

    private final RestaurantGateway restaurantGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public RestaurantResult execute(Long id, UpdateRestaurantCommand command) {
        RestaurantAggregate current = restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_ID_MSG + id));

        RestaurantAggregate toUpdate = new RestaurantAggregate(
                current.id(),
                command.name(),
                command.cuisineType(),
                command.openingTime(),
                command.closingTime(),
                command.address(),
                command.ownerId(),
                current.ownerName(),
                current.createdAt(),
                current.lastModifiedAt()
        );

        return RestaurantApplicationMapper.toResult(restaurantGateway.save(toUpdate));
    }
}
