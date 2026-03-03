package com.fiap.challenge.repository;

import com.fiap.challenge.entity.MenuItem;
import com.fiap.challenge.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    Optional<MenuItem> findByRestaurantAndName(Restaurant restaurant, String name);
}
