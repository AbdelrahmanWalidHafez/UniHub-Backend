package com.unihub.auth.security.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class VerificationResponse {

    @JsonProperty("verification-opaque-token")
    private VerificationOpaqueToken verificationOpaqueToken;

}

