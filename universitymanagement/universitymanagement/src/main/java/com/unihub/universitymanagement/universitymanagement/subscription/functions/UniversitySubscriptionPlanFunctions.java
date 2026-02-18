package com.unihub.universitymanagement.universitymanagement.subscription.functions;

import com.unihub.universitymanagement.universitymanagement.subscription.dto.request.SubscriptionPlan;
import com.unihub.universitymanagement.universitymanagement.subscription.service.ISubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class UniversitySubscriptionPlanFunctions {

    private final ISubscriptionService subscriptionService;

    @Bean
    public Consumer<SubscriptionPlan> setUniversitySubscriptionPlan() {
        return subscriptionService::update;
    }
}
