package com.example.oauth.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewUserDTO {

    @Email
    private String email;
    @Size(min = 8, max = 20)
    private String password;
}
