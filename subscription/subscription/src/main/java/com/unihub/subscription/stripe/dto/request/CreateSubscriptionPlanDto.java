package com.unihub.subscription.stripe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CreateSubscriptionPlanDto {

    @NotBlank(message = "Plan name must not be empty")
    @Size(min = 3, max = 50, message = "Plan name must be between 3 and 50 characters")
    @JsonProperty("subscription_plan_name")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Plan name must contain letters only and no numbers")
    private String planName;

    @NotBlank(message = "Plan description must not be empty")
    @Size(min = 10, max = 200, message = "Plan description must be between 10 and 1000 characters")
    @JsonProperty("subscription_plan_description")
    private String planDescription;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1 (smallest currency unit)")
    private Long price;

    @Min(value = 1, message = "Max user amount must be at least 1")
    @Max(value = 1_000_000, message = "Max user amount is too large")
    @JsonProperty("subscription_plan_max_user_amount")
    private int maxUserAmount;
}
