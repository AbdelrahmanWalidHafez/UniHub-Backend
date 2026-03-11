package com.unihub.subscription.subscriptionrequest.client;

import com.unihub.subscription.subscriptionrequest.client.fallback.UniversityFallBack;
import com.unihub.subscription.subscriptionrequest.dto.request.CreateUniversityRequest;
import com.unihub.subscription.subscriptionrequest.dto.response.UniversityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "universitymanagement", fallback = UniversityFallBack.class)
public interface UniversityFeignClient {

    @PostMapping(value = "/api/v1/internal/create", consumes = "application/json")
    ResponseEntity<UniversityResponse> createUniversity(@RequestBody CreateUniversityRequest request);

    @GetMapping(value = "/api/v1/internal/get-subscription-plan-count/{id}", consumes = "application/json")
    ResponseEntity<Long> getSubscriptionPlanCount(@PathVariable UUID id);
}
