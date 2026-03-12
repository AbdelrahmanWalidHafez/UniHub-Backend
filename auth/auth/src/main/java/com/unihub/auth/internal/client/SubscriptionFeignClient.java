package com.unihub.auth.internal.client;

import com.unihub.auth.internal.client.fallback.SubscriptionFeignClientFallBack;
import com.unihub.auth.internal.dto.response.SubscriptionPlanResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "subscription", fallback = SubscriptionFeignClientFallBack.class)
public interface SubscriptionFeignClient {

    @GetMapping(value = "/api/v1/subscription-plans/admin/{id}", consumes = "application/json")
    ResponseEntity<SubscriptionPlanResponseDto> getSubscriptionPlan(@PathVariable UUID id);

}