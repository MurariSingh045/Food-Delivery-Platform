package com.ms.AUTH_SERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private Long id;

    private String email;

    private List<String> roles;


}
