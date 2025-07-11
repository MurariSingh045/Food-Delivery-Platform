package com.ms.AUTH_SERVICE.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDTO {

    private String name;

    private String email;

    private String password;

}
