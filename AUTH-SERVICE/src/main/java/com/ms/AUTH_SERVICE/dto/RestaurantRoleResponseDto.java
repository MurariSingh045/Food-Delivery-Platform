package com.ms.AUTH_SERVICE.dto;

import com.ms.AUTH_SERVICE.model.RestaurantRoleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRoleResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String pan;
    private String aadhar;
    private String licence;
    private String gst;
    private RestaurantRoleStatus status;
    private String adminMessage;
    private LocalDateTime createdAt;


}
