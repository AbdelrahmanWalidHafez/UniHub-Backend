package com.unihub.subscription.subscriptionrequest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AfterUpdateResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("subscription-request")
    SubscriptionRequestResponseDto subscriptionRequestResponseDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String warn;
}
