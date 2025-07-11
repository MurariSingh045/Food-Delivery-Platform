package com.ms.AUTH_SERVICE.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    //private Set<String> roles;
}
