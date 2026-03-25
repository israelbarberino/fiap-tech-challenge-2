package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.application.restaurant.mapper.RestaurantApplicationMapper;
import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetRestaurantsByCuisineTypeUseCase {

    private final RestaurantGateway restaurantGateway;

    public GetRestaurantsByCuisineTypeUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<RestaurantResult> execute(String cuisineType) {
        return restaurantGateway.findByCuisineType(cuisineType).stream()
                .map(RestaurantApplicationMapper::toResult)
                .toList();
    }
}
