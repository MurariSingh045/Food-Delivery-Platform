package com.ms.AUTH_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPromotionResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;
}
