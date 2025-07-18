package com.ms.RESTAURANT_SERVICE.service;

import com.ms.RESTAURANT_SERVICE.dto.RestaurantRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.RestaurantResponseDto;
import com.ms.RESTAURANT_SERVICE.model.Restaurant;
import com.ms.RESTAURANT_SERVICE.repo.MenuItemRepository;
import com.ms.RESTAURANT_SERVICE.repo.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public RestaurantResponseDto addRestaurant(RestaurantRequestDto restaurantRequestDto,
                                               Long ownerId , String role) {
        // if the role is not "RESTAURANT" access denied
        if (!role.contains("RESTAURANT")) {

        }

        // Create and save
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequestDto.getName())
                .location(restaurantRequestDto.getLocation())
                .ownerId(ownerId)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);

        return RestaurantResponseDto.builder()
                .resId(saved.getId())
                .name(saved.getName())
                .location(saved.getLocation())
                .ownerId(saved.getOwnerId())
                .build();

    }

    public List<RestaurantResponseDto> findAllByOwnerId(Long ownerId , String role) {

        if (!role.contains("RESTAURANT")) {
            throw new RuntimeException("Access denied! Only Restaurant Owners can add a restaurant.");
        }


        List<Restaurant> restaurants  =  restaurantRepository.findAllByOwnerId(ownerId);

        List<RestaurantResponseDto> response = restaurants.stream()
                .map(r -> RestaurantResponseDto.builder()
                        .resId(r.getId())
                        .name(r.getName())
                        .location(r.getLocation())
                        .build())
                .toList();
        return response;
    }


    @Override
    public List<RestaurantResponseDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(r -> RestaurantResponseDto.builder()
                        .resId(r.getId())
                        .name(r.getName())
                        .location(r.getLocation())
                        .ownerId(r.getOwnerId())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Restaurant> findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    @Override
    public void deleteRestaurant(Long resId) {
        restaurantRepository.deleteById(resId);
    }

    @Transactional
    @Override
    public String deleteRestaurant(Long restaurantId, Long ownerId, String role) {
        // Check roles
        if (!role.contains("RESTAURANT")) {
            throw new RuntimeException("Access denied. You are not a restaurant owner.");
        }

        // Find restaurants
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Check ownership
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You can only delete your own restaurant.");
        }

        // Delete all menu items first
        menuItemRepository.deleteByRestaurantId(restaurantId);

        // Delete restaurant
        restaurantRepository.deleteById(restaurantId);

        return "Restaurant : " +restaurantId+ " : and related menu items deleted successfully.";
    }


    @Override
    public RestaurantResponseDto getRestaurantById(Long id) {

        // getting restaurants by id.
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        return RestaurantResponseDto.builder()
                .resId(restaurant.getId())
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .ownerId(restaurant.getOwnerId())
                .build();

    }

    @Override
    public long countRestaurants() {
        return restaurantRepository.count();
    }


}
