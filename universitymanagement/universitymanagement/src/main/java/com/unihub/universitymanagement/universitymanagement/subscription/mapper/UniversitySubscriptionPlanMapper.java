package com.unihub.universitymanagement.universitymanagement.subscription.mapper;

import com.unihub.universitymanagement.universitymanagement.subscription.dto.response.UniversitySubscriptionPlanResponse;
import com.unihub.universitymanagement.universitymanagement.subscription.model.UniversitySubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class UniversitySubscriptionPlanMapper {

    public UniversitySubscriptionPlanResponse toDto(UniversitySubscriptionPlan subscriptionPlan) {
        UniversitySubscriptionPlanResponse dto = new UniversitySubscriptionPlanResponse();
        dto.setId(subscriptionPlan.getId());
        dto.setPid(subscriptionPlan.getPid());
        dto.setEndDate(subscriptionPlan.getEndDate());
        dto.setStartDate(subscriptionPlan.getStartDate());
        return dto;
    }
}
