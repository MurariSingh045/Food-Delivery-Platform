package com.ms.ORDER_SERVICE.dto;

import com.ms.ORDER_SERVICE.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateResponseDto{

    private Long orderId;

    private OrderStatus status;

    private String message;

    private LocalDateTime updateAt;

}
