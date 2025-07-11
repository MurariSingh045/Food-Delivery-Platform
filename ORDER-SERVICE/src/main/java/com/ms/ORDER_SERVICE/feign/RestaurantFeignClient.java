package com.ms.ORDER_SERVICE.feign;


import com.ms.ORDER_SERVICE.dto.RestaurantResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESTAURANT-SERVICE")
public interface RestaurantFeignClient {


    @GetMapping("/restaurant/{id}")
    RestaurantResponseDto getRestaurantById(@PathVariable("id") Long restaurantId);

    @GetMapping("/restaurant/count")
    long getTotalRestaurants();
}
