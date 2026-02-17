package com.unihub.auth.security.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VerificationOpaqueToken {

    private String token;

    @JsonProperty("token_type")
    private String tokenType;

    private long expiresIn;
}
