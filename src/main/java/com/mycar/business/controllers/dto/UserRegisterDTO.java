package com.mycar.business.controllers.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
