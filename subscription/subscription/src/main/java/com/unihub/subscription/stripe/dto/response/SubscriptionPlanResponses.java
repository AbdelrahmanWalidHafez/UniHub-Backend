package com.unihub.subscription.stripe.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SubscriptionPlanResponses {
    @JsonProperty("subscription-plans")
    List<SubscriptionPlanResponseDto> plans;
}
