package com.ms.ORDER_SERVICE.dto;

import com.ms.ORDER_SERVICE.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequestDto {

   private OrderStatus status;
}
