package com.ms.AUTH_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String token;
    private String type = "Bearer";

    public JwtResponseDTO(String token) {
        this.token = token;
    }
}
