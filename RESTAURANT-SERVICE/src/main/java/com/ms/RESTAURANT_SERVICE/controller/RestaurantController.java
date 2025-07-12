package com.ms.RESTAURANT_SERVICE.controller;

import com.ms.RESTAURANT_SERVICE.dto.MenuItemResponseDto;
import com.ms.RESTAURANT_SERVICE.dto.RestaurantRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.RestaurantResponseDto;
import com.ms.RESTAURANT_SERVICE.model.Restaurant;
import com.ms.RESTAURANT_SERVICE.service.MenuItemService;
import com.ms.RESTAURANT_SERVICE.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemService menuItemService;

    // adding restaurant by Owner
    @PostMapping("/add")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto ,
                                           @RequestHeader("X-User-Id") Long ownerId ,
                                            @RequestHeader("X-User-Role") String role)
    {
        try{
            RestaurantResponseDto response = restaurantService.addRestaurant(restaurantRequestDto, ownerId, role);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Owner can delete restaurant
    @DeleteMapping("/delete/{resId}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long resId ,
                                              @RequestHeader("X-User-Id") Long ownerId,
                                              @RequestHeader("X-User-Role") String role)
    {
        try {
            String msg = restaurantService.deleteRestaurant(resId, ownerId, role);
            return ResponseEntity.ok(msg);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // find all restaurants
    //  Get all restaurants owned by the logged-in user
    @GetMapping("/my-restaurants")
    public ResponseEntity<?> getMyRestaurants(@RequestHeader("X-User-Id") Long ownerId,
                                              @RequestHeader("X-User-Role") String role) {

        try{
            // getting all restaurants here of specific Restaurants owner
            List<RestaurantResponseDto> response = restaurantService.findAllByOwnerId(ownerId , role);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Public: get all restaurants (for users)
    @GetMapping("/all")
    public ResponseEntity<?> getAllRestaurants() {

       try{
           List<RestaurantResponseDto> response = restaurantService.getAllRestaurants();
           return ResponseEntity.ok(response);
       }catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }
    }

    //owner can check their restaurants menuItems
    @GetMapping("/menu-items/{restaurantId}")
    public ResponseEntity<?> getMenuItemsFromOwner(@PathVariable Long restaurantId ,
                                                   @RequestHeader("X-User-Id") Long ownerId,
                                                   @RequestHeader("X-User-Role") String role
                                                  )
    {

        try{
            List<MenuItemResponseDto> items = menuItemService.getMenuItems(restaurantId , ownerId , role);
            return ResponseEntity.ok(items);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // get restaurant by id. which is called by feign client which is inside the Order service
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id)
    {

       RestaurantResponseDto restaurant = restaurantService.getRestaurantById(id);
       return ResponseEntity.ok(restaurant);
    }

    // get count of restaurant which is called via feign client inside the OrderService
    @GetMapping("/count")
    public long countRestaurants() {
        return restaurantService.countRestaurants();
    }


}
