package com.ms.ORDER_SERVICE.dto;

import com.ms.ORDER_SERVICE.model.OrderItem;
import com.ms.ORDER_SERVICE.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private Long restaurantId;

    private LocalDateTime orderTime;

    private OrderStatus orderStatus;

    private Double totalAmount;

    private List<OrderItemResponseDto> items;

}
