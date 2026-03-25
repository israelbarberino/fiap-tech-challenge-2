package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.mapper.RestaurantApplicationMapper;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetRestaurantByIdUseCase {

    private static final String RESTAURANT_NOT_FOUND_ID_MSG = "Restaurant not found with id: ";

    private final RestaurantGateway restaurantGateway;

    public GetRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public RestaurantResult execute(Long id) {
        return restaurantGateway.findById(id)
                .map(RestaurantApplicationMapper::toResult)
                .orElseThrow(() -> new ResourceNotFoundException(RESTAURANT_NOT_FOUND_ID_MSG + id));
    }
}
