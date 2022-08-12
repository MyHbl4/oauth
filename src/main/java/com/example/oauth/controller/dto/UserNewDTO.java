package com.example.oauth.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserNewDTO {

    @NotNull
    @Email
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    @Length(min = 8, max = 20)
    private String password;
}
