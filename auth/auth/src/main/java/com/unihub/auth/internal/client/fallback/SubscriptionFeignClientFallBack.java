package com.unihub.auth.internal.client.fallback;

import com.unihub.auth.internal.client.SubscriptionFeignClient;
import com.unihub.auth.internal.dto.response.SubscriptionPlanResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
public class SubscriptionFeignClientFallBack implements SubscriptionFeignClient {
    @Override
    public ResponseEntity<SubscriptionPlanResponseDto> getSubscriptionPlan(UUID id) {
        log.error("Feign client fallback triggered: Subscription service is unavailable. Request: {}","/api/v1/subscription-plans/"+ id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
