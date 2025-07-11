package com.ms.RESTAURANT_SERVICE.service;

import com.ms.RESTAURANT_SERVICE.dto.RestaurantRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.RestaurantResponseDto;
import com.ms.RESTAURANT_SERVICE.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {



    RestaurantResponseDto addRestaurant(RestaurantRequestDto restaurantRequestDto,
                                        Long ownerId , String role);

    List<RestaurantResponseDto> findAllByOwnerId(Long ownerId , String role);

    List<RestaurantResponseDto> getAllRestaurants();

    Optional<Restaurant> findById(Long restaurantId);

    void deleteRestaurant(Long resId);

    String deleteRestaurant(Long restaurantId, Long ownerId, String role);

    RestaurantResponseDto getRestaurantById(Long id);
}
