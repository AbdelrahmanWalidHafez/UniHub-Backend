package com.unihub.auth.internal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversitySubscriptionPlanResponse {
    @JsonProperty("record_id")
    private UUID id;

    @JsonProperty("plan_id")
    private UUID pid;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;
}
