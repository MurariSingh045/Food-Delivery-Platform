package com.ms.AUTH_SERVICE.controller;

import com.ms.AUTH_SERVICE.dto.RestaurantRoleRequestDto;
import com.ms.AUTH_SERVICE.dto.RestaurantRoleResponseDto;
import com.ms.AUTH_SERVICE.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant-role")
public class RestaurantRoleRequestController {

    @Autowired
    private RestaurantService restaurantService;


    // Request for a restaurant role.
    @PostMapping("/request")
    public ResponseEntity<?> restaurantRoleRequest(@RequestBody RestaurantRoleRequestDto restaurantRoleRequestDto,
                                                   @RequestHeader("X-User-Role") String role,
                                                   @RequestHeader("X-User-Email") String email)
    {

        // if the role is not user then user can't make restaurant request
        if(!"ROLE_USER".contains(role)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(" Access denied: Only User valid User can Request for Restaurant role !");

        }
        RestaurantRoleResponseDto response =  restaurantService.restaurantRoleRequest(restaurantRoleRequestDto , email);
        return ResponseEntity.ok(response);
    }

    // User can check the Restaurant role status whether it's approved or not
    @GetMapping("/status")
    public ResponseEntity<?> getRestaurantStatus(@RequestHeader("X-User-Email") String email)
    {
        RestaurantRoleResponseDto response =  restaurantService.getRestaurantStatus(email);
        return ResponseEntity.ok(response);
    }

}
