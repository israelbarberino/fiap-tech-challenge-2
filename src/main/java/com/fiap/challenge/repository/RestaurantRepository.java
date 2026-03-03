package com.fiap.challenge.repository;

import com.fiap.challenge.entity.Restaurant;
import com.fiap.challenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    Optional<Restaurant> findByOwner(User owner);
    List<Restaurant> findByCuisineType(String cuisineType);
}
