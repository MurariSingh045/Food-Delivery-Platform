package com.ms.ORDER_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDto {

     private Long resId;
     private String name;
     private String location;
     private Long ownerId;
}
