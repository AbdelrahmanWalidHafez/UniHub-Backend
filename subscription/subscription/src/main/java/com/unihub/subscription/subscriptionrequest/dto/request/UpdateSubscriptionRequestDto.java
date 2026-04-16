package com.unihub.subscription.subscriptionrequest.dto.request;

import com.unihub.subscription.subscriptionrequest.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateSubscriptionRequestDto {

    @NotNull(message = "status cannot be null")
    private Status status;
}
