package com.fiap.challenge.domain.restaurant.port;

import com.fiap.challenge.domain.restaurant.model.RestaurantAggregate;

import java.util.List;
import java.util.Optional;

public interface RestaurantGateway {
    RestaurantAggregate save(RestaurantAggregate aggregate);

    Optional<RestaurantAggregate> findById(Long id);

    Optional<RestaurantAggregate> findByName(String name);

    List<RestaurantAggregate> findAll();

    List<RestaurantAggregate> findByCuisineType(String cuisineType);

    void deleteById(Long id);
}
