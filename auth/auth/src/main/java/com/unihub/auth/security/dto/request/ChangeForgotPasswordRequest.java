package com.unihub.auth.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeForgotPasswordRequest {
    @NotBlank(message = "password cannot be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message ="Password must have uppercase, lowercase, digit, special character, and be at least 8 characters long."
    )
    String password;

    @JsonProperty("confirm_password")
    @NotBlank(message = "confirm password cannot be blank")
    String confirmPassword;
}
