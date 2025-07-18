package com.ms.ORDER_SERVICE.service;

import com.ms.ORDER_SERVICE.dto.*;
import com.ms.ORDER_SERVICE.feign.AuthFeignClient;
import com.ms.ORDER_SERVICE.feign.RestaurantFeignClient;
import com.ms.ORDER_SERVICE.model.Order;
import com.ms.ORDER_SERVICE.model.OrderItem;
import com.ms.ORDER_SERVICE.model.OrderStatus;
import com.ms.ORDER_SERVICE.repo.OrderItemRepository;
import com.ms.ORDER_SERVICE.repo.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantFeignClient restaurantFeignClient;
    
    @Autowired
    private AuthFeignClient authFeignClient;

    // place order
    @Transactional
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, Long userId) {

         List<OrderItem> items = new ArrayList<>();
         double totalPrice = 0;

        for (OrderItemRequestDto itemDto : orderRequestDto.getItems()) {

            //  Fetch item details from Restaurant Service (trusted source)
            MenuItemResponseDto menuItem = restaurantFeignClient.getItemById(itemDto.getItemId());

            //  Create OrderItem with trusted price & name
            OrderItem orderItem = OrderItem.builder()
                    .itemName(menuItem.getName())
                    .price(menuItem.getPrice())
                    .quantity(itemDto.getQuantity())
                    .build();

            totalPrice += orderItem.getPrice() * orderItem.getQuantity();

            items.add(orderItem); // add item into item Order Item List
        }

        //  Create Order object
        Order order = Order.builder()
                .userId(userId)
                .restaurantId(orderRequestDto.getRestaurantId())
                .orderTime(LocalDateTime.now())
                .status(OrderStatus.PLACED)
                .totalAmount(totalPrice)
                .build();

        //  Set reverse relationship
        // to know which orderItem belongs to which Order
        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        //  Save order (cascade saves items too)
        Order saved = orderRepository.save(order);

        //  Build response
        List<OrderItemResponseDto> responseItems = saved.getItems().stream()
                .map(item -> OrderItemResponseDto.builder()
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        return OrderResponseDto.builder()
                .orderId(saved.getId())
                .userId(saved.getUserId())
                .restaurantId(saved.getRestaurantId())
                .orderTime(saved.getOrderTime())
                .orderStatus(saved.getStatus())
                .totalAmount(saved.getTotalAmount())
                .items(responseItems)
                .build();
    }

    @Override
    public List<OrderResponseDto> getOrderByUser(Long userId) {

        // extracting orders of user via userId
         List<Order> orders = orderRepository.findByUserId(userId);

         //extracting orderItems for each Order
         return orders.stream().map(order ->{
             List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                     .map(item-> OrderItemResponseDto.builder()
                             .itemName(item.getItemName())
                             .quantity(item.getQuantity())
                             .price(item.getPrice())
                             .build())
                     .toList();

             // making Order Response of each Order
             return OrderResponseDto.builder()
                     .orderId(order.getId())
                     .userId(order.getUserId())
                     .restaurantId(order.getRestaurantId())
                     .orderTime(order.getOrderTime())
                     .orderStatus(order.getStatus())
                     .totalAmount(order.getTotalAmount())
                     .items(itemDtos)
                     .build();
         }).toList();
    }

    @Override
    public OrderStatusUpdateResponseDto updateOrderStatus(Long orderId, OrderStatus status, Long ownerId) {

        // find order by orderId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Fetch restaurant from Restaurant Service
        RestaurantResponseDto restaurant = restaurantFeignClient.getRestaurantById(order.getRestaurantId());

        // Check if the user owns this restaurant
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You can only update orders from your own restaurant");
        }

        // set orderStatus her
        order.setStatus(status);

        orderRepository.save(order); // save new status into db

        return OrderStatusUpdateResponseDto.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .message("Order status has been updated !")
                .updateAt(order.getOrderTime())
                .build();

    }

    @Override
    public List<OrderResponseDto> checkOrdersByRestaurantOwner(Long resId, Long ownerId) {

        // getting restaurant
        RestaurantResponseDto restaurant = restaurantFeignClient.getRestaurantById(resId);

        // check the restaurant has their right owner
        if(!restaurant.getOwnerId().equals(ownerId)){
            throw new RuntimeException("You can only update orders from your own restaurant");
        }

        // getting all order of restaurant here
        List<Order> orders = orderRepository.findByRestaurantId(resId);

        // extract order item from each order
        return orders.stream().map(order -> {
            List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                    .map(item->OrderItemResponseDto.builder()
                            .itemName(item.getItemName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build())
                    .toList();

            // making order response of  each order
            return OrderResponseDto.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .restaurantId(order.getRestaurantId())
                    .orderTime(order.getOrderTime())
                    .orderStatus(order.getStatus())
                    .totalAmount(order.getTotalAmount())
                    .items(itemDtos)
                    .build();
        }).toList();

    }

    @Override
    public OrderStatusUpdateResponseDto cancelOrderByUser(Long orderId, Long userId) {

        // check order is found by id or not
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        // if the user id that we are providing and user id from order doesn't match then that user can't cancel the order
        if(!order.getUserId().equals(userId))
        {
            throw new RuntimeException("You can only cancel your own orders");
        }

        // only the place order can be cancelled
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only orders with status PLACED can be cancelled");
        }


        order.setStatus(OrderStatus.CANCELLED); // cancelling order

        Order updated = orderRepository.save(order); // update changes in DB

        return OrderStatusUpdateResponseDto.builder()
                .orderId(updated.getId())
                .status(updated.getStatus())
                .message("Order cancelled successfully")
                .updateAt(LocalDateTime.now())
                .build();
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByAdmin() {

        List<Order> orders = orderRepository.findAll();

        //extracting orderItems for each Order
        return orders.stream().map(order ->{
            List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                    .map(item-> OrderItemResponseDto.builder()
                            .itemName(item.getItemName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build())
                    .toList();

            // making Order Response of each Order
            return OrderResponseDto.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .restaurantId(order.getRestaurantId())
                    .orderTime(order.getOrderTime())
                    .orderStatus(order.getStatus())
                    .totalAmount(order.getTotalAmount())
                    .items(itemDtos)
                    .build();
        }).toList();
    }

    @Override
    public AdminStatsResponseDto getAdminStats() {

        long totalOrders = orderRepository.count();
        long placed = orderRepository.countByStatus(OrderStatus.PLACED);
        long inProgress = orderRepository.countByStatus(OrderStatus.IN_PROGRESS);
        long delivered = orderRepository.countByStatus(OrderStatus.DELIVERED);
        long cancelled = orderRepository.countByStatus(OrderStatus.CANCELLED);

        // Feign clients or REST calls to other services:
        long totalUsers = authFeignClient.getTotalUsers();// all users count from auth service
        long totalRestaurants = restaurantFeignClient.getTotalRestaurants(); // all restaurants count from Restaurant Service

        return AdminStatsResponseDto.builder()
                .totalOrders(totalOrders)
                .placedOrders(placed)
                .inProgressOrders(inProgress)
                .deliveredOrders(delivered)
                .cancelledOrders(cancelled)
                .totalUsers(totalUsers)
                .totalRestaurants(totalRestaurants)
                .build();
    }

    @Override
    public OrderStatusUpdateResponseDto cancelOrderByAdmin(Long orderId) {

        // check order is found by id or not
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // You can allow cancel at any stage — or restrict e.g., not after DELIVERED
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a delivered order");
        }

        order.setStatus(OrderStatus.ADMIN_CANCELLED); // cancel order forcefully by Admin.

        Order updated = orderRepository.save(order); // save updated order status



        return OrderStatusUpdateResponseDto.builder()
                .orderId(updated.getId())
                .status(updated.getStatus())
                .message("Order has been forcefully cancelled by admin")
                .updateAt(LocalDateTime.now())
                .build();

    }
}
