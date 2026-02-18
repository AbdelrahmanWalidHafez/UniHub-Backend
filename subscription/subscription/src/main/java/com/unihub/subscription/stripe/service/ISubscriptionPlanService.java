package com.unihub.subscription.stripe.service;

import com.unihub.subscription.stripe.dto.request.CreateSubscriptionPlanDto;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.stripe.model.SubscriptionPlan;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ISubscriptionPlanService {

    SubscriptionPlanResponseDto createSubscriptionPlan(CreateSubscriptionPlanDto createSubscriptionPlanDto);

    SubscriptionPlanResponseDto updatePlan(CreateSubscriptionPlanDto createSubscriptionPlanDto, UUID id);

    SubscriptionPlan getPlan(UUID id);

    List<SubscriptionPlanResponseDto> getAllSubscriptionPlans(int pageNum, String sortDir, String sortField);

    Void setUniversityPlan(HttpServletRequest request, UUID subscriptionPlanId);

    void deleteSubscription(UUID id);
}
