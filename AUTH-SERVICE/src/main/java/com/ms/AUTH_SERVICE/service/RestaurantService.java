package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.dto.RestaurantRoleRequestDto;
import com.ms.AUTH_SERVICE.dto.RestaurantRoleResponseDto;

import java.util.List;

public interface RestaurantService {
    RestaurantRoleResponseDto restaurantRoleRequest(RestaurantRoleRequestDto restaurantRoleRequestDto , String email);

    RestaurantRoleResponseDto getRestaurantStatus(String email);

    List<RestaurantRoleResponseDto> getAllRestaurantRequests();

    RestaurantRoleResponseDto approveRestaurantRoleRequest(Long id);

    RestaurantRoleResponseDto rejectRestaurantRoleRequest(Long requestId);
}
