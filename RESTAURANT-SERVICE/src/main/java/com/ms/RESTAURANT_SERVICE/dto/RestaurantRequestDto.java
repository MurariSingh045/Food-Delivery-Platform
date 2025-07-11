package com.ms.RESTAURANT_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRequestDto {

    private  String name; // restaurant name
    private String location; // restaurant location
}
