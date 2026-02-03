package com.unihub.subscription.stripe.mapper;

import com.unihub.subscription.stripe.dto.request.CreateSubscriptionPlanDto;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.stripe.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlan toEntity(CreateSubscriptionPlanDto createSubscriptionPlanDto){
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setPlanName(createSubscriptionPlanDto.getPlanName());
        subscriptionPlan.setPlanDescription(createSubscriptionPlanDto.getPlanDescription());
        subscriptionPlan.setPrice(createSubscriptionPlanDto.getPrice());
        subscriptionPlan.setMaxUserAmount(createSubscriptionPlanDto.getMaxUserAmount());
        subscriptionPlan.setCurrency("USD");
        subscriptionPlan.setBillingCycle("YEARLY");
        return subscriptionPlan;
    }

    public SubscriptionPlanResponseDto toDto(SubscriptionPlan subscriptionPlan){
        SubscriptionPlanResponseDto subscriptionPlanResponseDto = new SubscriptionPlanResponseDto();
        subscriptionPlanResponseDto.setSid(subscriptionPlan.getSid());
        subscriptionPlanResponseDto.setPlanName(subscriptionPlan.getPlanName());
        subscriptionPlanResponseDto.setPlanDescription(subscriptionPlan.getPlanDescription());
        subscriptionPlanResponseDto.setPrice(subscriptionPlan.getPrice());
        subscriptionPlanResponseDto.setMaxUserAmount(subscriptionPlan.getMaxUserAmount());
        subscriptionPlanResponseDto.setCurrency(subscriptionPlan.getCurrency());
        subscriptionPlanResponseDto.setBillingCycle(subscriptionPlan.getBillingCycle());
        subscriptionPlanResponseDto.setCreatedAt(subscriptionPlan.getCreatedAt());
        subscriptionPlanResponseDto.setUpdatedAt(subscriptionPlan.getUpdatedAt());
        subscriptionPlanResponseDto.setCreatedBy(subscriptionPlan.getCreatedBy());
        subscriptionPlanResponseDto.setUpdatedBy(subscriptionPlan.getUpdatedBy());
        return subscriptionPlanResponseDto;
    }
}
