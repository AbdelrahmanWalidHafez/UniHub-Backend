package com.unihub.auth.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenRequest {

    @JsonProperty("refresh_token")
    private String refreshToken;

}
