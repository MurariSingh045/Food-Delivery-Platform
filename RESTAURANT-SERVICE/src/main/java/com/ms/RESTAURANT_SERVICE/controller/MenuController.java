package com.ms.RESTAURANT_SERVICE.controller;

import com.ms.RESTAURANT_SERVICE.dto.MenuItemRequestDto;
import com.ms.RESTAURANT_SERVICE.dto.MenuItemResponseDto;
import com.ms.RESTAURANT_SERVICE.service.MenuItemService;
import com.ms.RESTAURANT_SERVICE.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/restaurant/menu")
public class MenuController {

       @Autowired
       private MenuItemService menuItemService;

       @Autowired
       private RestaurantService restaurantService;

       // add menu items
       @PostMapping("/add-menu")
      public ResponseEntity<?> addMenuItem(@RequestBody MenuItemRequestDto menuItemRequestDto ,
                                           @RequestHeader("X-User-Id") Long ownerId ,
                                           @RequestHeader("X-User-Role") String role)
      {

         try {
             MenuItemResponseDto response = menuItemService.addMenuItem(menuItemRequestDto , ownerId , role);
             return ResponseEntity.ok(response); // return MenuItemResponsedto object
         }catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
         }
      }

      // update menu items
     @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMenuItems(@PathVariable Long id ,
                                             @RequestBody MenuItemRequestDto newMenuItemRequestDto ,
                                             @RequestHeader("X-User-Id") Long ownerId,
                                             @RequestHeader("X-User-Role") String role)
     {

        try {
            // update menu
            MenuItemResponseDto response =  menuItemService.updateMenuItems(id, newMenuItemRequestDto , ownerId,role);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
     }

     // delete Menu Item
     @DeleteMapping("/delete/{id}")
     public ResponseEntity<?> deleteMenuItem(@PathVariable Long id,
                                             @RequestHeader("X-User-Id") Long ownerId,
                                             @RequestHeader("X-User-Role") String role)
     {
         try {
             // delete menu
             menuItemService.deleteMenuItem(id , ownerId , role);
             return ResponseEntity.ok("Deleted successfully");
         }catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
         }
     }

}
