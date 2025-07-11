package com.ms.ORDER_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    private String itemName;
    private Integer quantity;
    private Double price;
}
