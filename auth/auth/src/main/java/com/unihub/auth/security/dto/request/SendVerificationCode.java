package com.unihub.auth.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SendVerificationCode {

    private String to;

    @JsonProperty("verification_code")
    private String verificationCode;

    @JsonProperty("expiration_time")
    private long expirationTime;
}
