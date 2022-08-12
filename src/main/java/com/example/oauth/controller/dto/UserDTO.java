package com.example.oauth.controller.dto;

import com.example.oauth.model.Provider;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDTO {

    private Long id;

    @NotNull
    @Email
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    @Length(min = 8, max = 20)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String username;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Provider provider;
}
