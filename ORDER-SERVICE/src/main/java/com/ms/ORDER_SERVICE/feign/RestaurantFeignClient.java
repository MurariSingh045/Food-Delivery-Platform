package com.ms.ORDER_SERVICE.feign;


import com.ms.ORDER_SERVICE.dto.MenuItemResponseDto;
import com.ms.ORDER_SERVICE.dto.RestaurantResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESTAURANT-SERVICE")
public interface RestaurantFeignClient {

    // get Restaurant
    @GetMapping("/restaurant/{id}")
    RestaurantResponseDto getRestaurantById(@PathVariable("id") Long restaurantId);

    // get count of restaurant
    @GetMapping("/restaurant/count")
    long getTotalRestaurants();

    // get MenuItem via item id
    @GetMapping("/restaurant/menu/item/{id}")
    MenuItemResponseDto getItemById(@PathVariable("id") Long itemId);
}
