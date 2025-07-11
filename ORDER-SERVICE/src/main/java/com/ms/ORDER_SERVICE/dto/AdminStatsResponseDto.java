package com.ms.ORDER_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsResponseDto {
    private long totalOrders;
    private long placedOrders;
    private long inProgressOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private long totalUsers;
    private long totalRestaurants;
}
