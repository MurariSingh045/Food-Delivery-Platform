package com.ms.ORDER_SERVICE.service;

import com.ms.ORDER_SERVICE.dto.*;
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

    // place order
    @Transactional
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, Long userId) {

        // extracting order items from Order
        List<OrderItem> items = orderRequestDto.getItems().stream()
                .map(item -> OrderItem.builder()
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        // calculating price of each item
        double totalPrice = items.stream()
                .mapToDouble(item-> item.getPrice() * item.getQuantity())
                .sum();

        // making object of Order and putting order items on it
        Order order = Order.builder()
                .userId(userId)
                .restaurantId(orderRequestDto.getRestaurantId())
                .orderTime(LocalDateTime.now())
                .status(OrderStatus.PLACED)
                .totalAmount(totalPrice)
                .build();

          // setting reverse relation
        // this will tell JPA that  which items belongs to which order.
        // Order → OrderItems (parent(Order) should know about its children(OrderItems)
        //OrderItem → Order (children(orderItems) should must have info about its Parent(Order)
         items.forEach(i->i.setOrder(order));
         order.setItems(items);

        Order saved =  orderRepository.save(order); // saves items too due to CascadeType.ALL

        // extract items from saved Order
        List<OrderItem> itemList = saved.getItems();

        // making OrderItemResponseDto Object of item List
       List<OrderItemResponseDto> responseItems = items.stream()
               .map(item -> OrderItemResponseDto.builder()
                       .itemName(item.getItemName())
                       .quantity(item.getQuantity())
                       .price(item.getPrice())
                       .build())
               .toList();


       // return Order Response
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
}
