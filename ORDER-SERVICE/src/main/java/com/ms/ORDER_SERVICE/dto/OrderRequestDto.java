package com.ms.ORDER_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Long restaurantId;

    private List<OrderItemRequestDto> items;
}
