package com.ms.AUTH_SERVICE.controller;

import com.ms.AUTH_SERVICE.dto.AdminPromotionResponseDto;
import com.ms.AUTH_SERVICE.dto.RestaurantRoleResponseDto;
import com.ms.AUTH_SERVICE.repo.RoleRepository;
import com.ms.AUTH_SERVICE.repo.UserRepository;
import com.ms.AUTH_SERVICE.service.AdminService;
import com.ms.AUTH_SERVICE.service.RestaurantService;
import com.ms.AUTH_SERVICE.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final List<String> VALID_ROLES = List.of("USER", "ADMIN", "RESTAURANT");
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RestaurantService restaurantService;


//    @PutMapping("/assign-role")
//    public ResponseEntity<?> promoteUserToRestaurant(
//            @RequestParam String email,
//            @RequestParam String role
//    ) {
//        AdminPromotionResponseDto response = adminService.promoteUserToRestaurant(email , role);
//        return ResponseEntity.ok(response);
//    }



    // register admin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/promote-admin")
    public ResponseEntity<?> promoteUserToAdmin(@RequestParam String email,
                                                @RequestParam String role
                                                )
    {

        try {
            AdminPromotionResponseDto response = adminService.promoteUserToAdmin(email, role);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // admin is getting all pending requests here
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurant-requests")
    public ResponseEntity<?> getAllRestaurantRequests()
    {
        List<RestaurantRoleResponseDto> response = restaurantService.getAllRestaurantRequests();
        return ResponseEntity.ok(response);
    }

    //approve  the restaurant role request by admin
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveRestaurantRoleRequest(@PathVariable Long id)
    {
        RestaurantRoleResponseDto response = restaurantService.approveRestaurantRoleRequest(id);
        return ResponseEntity.ok(response);
    }


   // reject the restaurant role request by admin
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectRestaurantRoleRequest(@PathVariable Long requestId)
    {
        RestaurantRoleResponseDto response = restaurantService.rejectRestaurantRoleRequest(requestId);
        return ResponseEntity.ok(response);
    }




}
