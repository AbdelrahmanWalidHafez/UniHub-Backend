package com.unihub.auth.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivationRequest{

    @Email(message = "The entered value should be a valid email")
    @NotBlank(message = "Email Field cannot be blank")
    private String email;

}
