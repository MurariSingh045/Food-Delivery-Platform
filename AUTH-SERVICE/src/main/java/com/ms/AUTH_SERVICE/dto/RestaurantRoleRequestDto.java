package com.ms.AUTH_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRoleRequestDto {

    private String phone;
    private String address;
    private String pan;
    private String aadhar;
    private String licence;
    private String gst;

}
