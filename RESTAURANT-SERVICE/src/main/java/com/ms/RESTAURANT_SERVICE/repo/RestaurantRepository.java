package com.ms.RESTAURANT_SERVICE.repo;

import com.ms.RESTAURANT_SERVICE.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant , Long> {

    List<Restaurant> findAllByOwnerId(Long ownerId);

}
