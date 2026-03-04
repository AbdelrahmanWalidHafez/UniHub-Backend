package com.unihub.auth.security.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty("access_token")
    private AccessToken accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("refresh_token")
    private RefreshToken refreshToken;
}