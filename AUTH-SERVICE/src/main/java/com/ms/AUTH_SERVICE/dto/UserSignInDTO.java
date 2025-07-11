package com.ms.AUTH_SERVICE.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInDTO {

    private String email;

    private String password;

}
