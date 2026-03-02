package com.unihub.usage.client.fallback;

import com.unihub.usage.client.AuthFeignClient;
import com.unihub.usage.dto.response.DashBoardAggregatesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class AuthFallBack implements AuthFeignClient {
    @Override
    public ResponseEntity<DashBoardAggregatesDto> getUserAnalysis(UUID tid, String apiKey) {
        log.error("Feign client fallback triggered: Auth service is unavailable.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
