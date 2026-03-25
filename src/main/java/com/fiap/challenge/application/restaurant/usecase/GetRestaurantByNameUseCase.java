package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.mapper.RestaurantApplicationMapper;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetRestaurantByNameUseCase {

    private static final String RESTAURANT_NOT_FOUND_NAME_MSG = "Restaurant not found with name: ";

    private final RestaurantGateway restaurantGateway;

    public GetRestaurantByNameUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public RestaurantResult execute(String name) {
        return restaurantGateway.findByName(name)
                .map(RestaurantApplicationMapper::toResult)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_NAME_MSG + name));
    }
}
