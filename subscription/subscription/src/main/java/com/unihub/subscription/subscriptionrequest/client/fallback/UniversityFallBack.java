package com.unihub.subscription.subscriptionrequest.client.fallback;

import com.unihub.subscription.subscriptionrequest.client.UniversityFeignClient;
import com.unihub.subscription.subscriptionrequest.dto.request.CreateUniversityRequest;
import com.unihub.subscription.subscriptionrequest.dto.response.UniversityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class UniversityFallBack implements UniversityFeignClient {

    @Override
    public ResponseEntity<UniversityResponse> createUniversity(CreateUniversityRequest createUniversityRequest) {
        log.error("Feign client fallback triggered: University service is unavailable. Request: {}", createUniversityRequest);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<Long> getSubscriptionPlanCount(UUID id) {
        log.error("Feign client fallback triggered: University service is unavailable. id: {}", id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
