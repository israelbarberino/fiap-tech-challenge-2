package com.fiap.challenge.application.restaurant.mapper;

import com.fiap.challenge.application.restaurant.dto.RestaurantResult;
import com.fiap.challenge.domain.restaurant.model.RestaurantAggregate;

public final class RestaurantApplicationMapper {

    private RestaurantApplicationMapper() {
    }

    public static RestaurantResult toResult(RestaurantAggregate aggregate) {
        return new RestaurantResult(
                aggregate.id(),
                aggregate.name(),
                aggregate.cuisineType(),
                aggregate.openingTime(),
                aggregate.closingTime(),
                aggregate.address(),
                aggregate.ownerId(),
                aggregate.ownerName(),
                aggregate.createdAt(),
                aggregate.lastModifiedAt()
        );
    }
}
