package com.ms.ORDER_SERVICE.controller;

import com.ms.ORDER_SERVICE.dto.*;
import com.ms.ORDER_SERVICE.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // place order by User
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDto orderRequestDto ,
                                        @RequestHeader("X-User-Id") Long userId,
                                        @RequestHeader("X-User-Role") String role)
    {

        // if the role is not user then he can't place order
        if (!role.contains("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only users can place orders");
        }

        try{
            OrderResponseDto orderResponse = orderService.placeOrder(orderRequestDto, userId);
            return ResponseEntity.ok(orderResponse);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    // User check their Order
    @GetMapping("/my-order")
    public  ResponseEntity<?> getMyOrders(@RequestHeader("X-User-Id") Long userId ,
                                                               @RequestHeader("X-User-Role") String role)
    {
        // if the role is not user then he can't getOrder
        if (!role.contains("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only users can place orders");
        }

        try {
            List<OrderResponseDto> orderResponse = orderService.getOrderByUser(userId);
            return ResponseEntity.ok(orderResponse);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // update order status by restaurant owner
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId ,
                                               @RequestBody OrderStatusUpdateRequestDto orderStatusUpdateRequestDto,
                                               @RequestHeader("X-User-Id") Long ownerId ,
                                               @RequestHeader("X-User-Role") String role)
    {

        // if the role is not restaurant
        if(!role.contains("RESTAURANT"))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only restaurant owners can update order status");
        }

        try{
            OrderStatusUpdateResponseDto response = orderService.updateOrderStatus(orderId , orderStatusUpdateRequestDto.getStatus() , ownerId);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // restaurant owner can check the orders of each restaurant

    @GetMapping("/restaurant-orders/{resId}")
    public ResponseEntity<?> checkOrdersByRestaurantOwner(@PathVariable Long resId,
                                                          @RequestHeader("X-User-Id") Long ownerId,
                                                          @RequestHeader("X-User-Role") String role)
    {
        // if the role is not restaurant
        if(!role.contains("RESTAURANT"))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only restaurant owners can see all orders ");
        }

        try {
            List<OrderResponseDto> response = orderService.checkOrdersByRestaurantOwner(resId , ownerId);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    // user cancel order
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrderByUser(@PathVariable Long orderId ,
                                               @RequestHeader("X-User-Id") Long userId ,
                                               @RequestHeader("X-User-Role") String role)
    {
        // only user can cancel order
        if (!role.contains("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only users can cancel orders");
        }

        try {
            OrderStatusUpdateResponseDto response = orderService.cancelOrderByUser(orderId , userId);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    //admin can view all orders
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrdersByAdmin(@RequestHeader("X-User-Role") String role)
    {
        // if the role is not Admin
        // only user can cancel order
        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only Admin can check all orders");
        }

       try {
           List<OrderResponseDto> response = orderService.getAllOrdersByAdmin();
           return ResponseEntity.ok(response);
       }catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }

    }

    // admin can have all data about orders , restaurant ,  users , order status
    @GetMapping("/admin/stats")
    public ResponseEntity<?> getPlatformStats(@RequestHeader("X-User-Role") String role)
    {
        // if the role is not admin
        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can view platform stats");
        }

        try {
            AdminStatsResponseDto stats = orderService.getAdminStats();
            return ResponseEntity.ok(stats);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
