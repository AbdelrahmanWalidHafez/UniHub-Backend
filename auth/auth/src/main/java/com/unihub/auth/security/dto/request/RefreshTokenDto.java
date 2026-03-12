package com.unihub.auth.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenDto {

    @JsonProperty("refresh_token")
    @NotBlank(message = "refresh token is required")
    private String refreshToken;

}
