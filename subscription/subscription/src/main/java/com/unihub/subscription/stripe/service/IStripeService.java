package com.unihub.subscription.stripe.service;

import com.unihub.subscription.stripe.dto.response.StripeResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface IStripeService {

    StripeResponse checkoutPlan(HttpServletRequest request, UUID id);

}
