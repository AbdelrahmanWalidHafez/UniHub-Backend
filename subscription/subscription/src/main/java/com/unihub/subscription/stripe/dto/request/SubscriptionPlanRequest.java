package com.unihub.subscription.stripe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionPlanRequest {

    @JsonProperty("plan_id")
    private UUID pid;

    @JsonProperty("university_id")
    private UUID universityID;

    @JsonProperty("start_date")
    private LocalDate  startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;
}
