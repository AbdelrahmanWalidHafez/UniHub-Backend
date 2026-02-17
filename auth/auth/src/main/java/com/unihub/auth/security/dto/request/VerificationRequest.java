package com.unihub.auth.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationRequest {

    @JsonProperty("verification_code")
    @NotBlank(message="verification code cannot be blank")
    String verificationCode;

}
