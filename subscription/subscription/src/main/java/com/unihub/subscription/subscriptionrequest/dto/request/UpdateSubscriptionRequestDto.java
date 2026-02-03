package com.unihub.subscription.subscriptionrequest.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateSubscriptionRequestDto {
    @Pattern(
            regexp = "PENDING|APPROVED|REJECTED",
            message = "Status must be PENDING, APPROVED, or REJECTED"
    )
    private String status;
}
