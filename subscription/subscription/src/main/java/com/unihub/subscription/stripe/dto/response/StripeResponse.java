package com.unihub.subscription.stripe.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StripeResponse {

    private String status;

    private String message;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("session_url")
    private String sessionUrl;
}