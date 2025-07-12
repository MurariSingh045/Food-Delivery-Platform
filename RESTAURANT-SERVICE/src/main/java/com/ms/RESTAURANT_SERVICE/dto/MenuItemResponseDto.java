package com.ms.RESTAURANT_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponseDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long restaurantId;
}
