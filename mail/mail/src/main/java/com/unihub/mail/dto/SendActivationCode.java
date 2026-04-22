package com.unihub.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SendActivationCode {

    private String to;

    @JsonProperty("activation_code")
    private String activationCode;

    @JsonProperty("expiration_time")
    private long expirationTime;

}
