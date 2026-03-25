package com.fiap.challenge.application.restaurant.usecase;

import com.fiap.challenge.domain.restaurant.port.RestaurantGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteRestaurantUseCase {

    private static final String RESTAURANT_NOT_FOUND_ID_MSG = "Restaurant not found with id: ";

    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(Long id) {
        if (restaurantGateway.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(RESTAURANT_NOT_FOUND_ID_MSG + id);
        }
        restaurantGateway.deleteById(id);
    }
}
