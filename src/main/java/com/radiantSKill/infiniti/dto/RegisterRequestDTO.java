package com.radiantSKill.infiniti.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role; // STUDENT / TEACHER / ADMIN / PRINCIPAL
}
