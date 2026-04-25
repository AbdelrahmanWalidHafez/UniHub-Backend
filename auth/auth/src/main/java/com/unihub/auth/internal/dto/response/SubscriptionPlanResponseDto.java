package com.unihub.auth.internal.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponseDto {

    @JsonProperty("subscription_plan_id")
    private UUID sid;

    @JsonProperty("subscription_plan_name")
    private String planName;

    @JsonProperty("subscription_plan_description")
    private String planDescription;

    @JsonProperty("subscription_plan_billing_cycle")
    private String billingCycle;

    @JsonProperty("subscription_plan_price")
    private Long price;

    @JsonProperty("subscription_plan_currency")
    private String currency;

    @JsonProperty("subscription_plan_max_user_amount")
    private int maxUserAmount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;
}