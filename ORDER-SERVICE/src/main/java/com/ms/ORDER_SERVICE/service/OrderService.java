package com.ms.ORDER_SERVICE.service;

import com.ms.ORDER_SERVICE.dto.OrderRequestDto;
import com.ms.ORDER_SERVICE.dto.OrderResponseDto;
import com.ms.ORDER_SERVICE.dto.OrderStatusUpdateResponseDto;
import com.ms.ORDER_SERVICE.model.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto , Long userId);

    List<OrderResponseDto> getOrderByUser(Long userId);

    OrderStatusUpdateResponseDto updateOrderStatus(Long orderId, OrderStatus status, Long ownerId);

    List<OrderResponseDto> checkOrdersByRestaurantOwner(Long resId, Long ownerId);

    OrderStatusUpdateResponseDto cancelOrderByUser(Long orderId, Long userId);
}
