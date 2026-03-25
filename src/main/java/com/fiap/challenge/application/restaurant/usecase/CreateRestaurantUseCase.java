package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.application.restaurant.dto.CreateRestaurantCommand;
import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.mapper.RestaurantApplicationMapper;
import com.fiap.challenge.domain.restaurant.model.RestaurantAggregate;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public RestaurantResult execute(CreateRestaurantCommand command) {
        RestaurantAggregate toCreate = new RestaurantAggregate(
                null,
                command.name(),
                command.cuisineType(),
                command.openingTime(),
                command.closingTime(),
                command.address(),
                command.ownerId(),
                null,
                null,
                null
        );
        return RestaurantApplicationMapper.toResult(restaurantGateway.save(toCreate));
    }
}
