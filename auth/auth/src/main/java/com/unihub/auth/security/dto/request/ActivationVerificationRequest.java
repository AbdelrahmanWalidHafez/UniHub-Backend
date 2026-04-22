package com.unihub.auth.security.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivationVerificationRequest {

    @JsonProperty("activation_code")
    @NotBlank(message="activation code cannot be blank")
    String activationCode;

    @Email
    @NotBlank
    String email;
}