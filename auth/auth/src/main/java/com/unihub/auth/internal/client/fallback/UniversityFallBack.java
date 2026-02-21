package com.unihub.auth.internal.client.fallback;

import com.unihub.auth.internal.client.UniversityFeignClient;
import com.unihub.auth.internal.dto.response.UniversityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class UniversityFallBack implements UniversityFeignClient {

    @Override
    public ResponseEntity<UniversityResponse> fetchUniversity(UUID id) {
        log.error("Feign client fallback triggered: University service is unavailable. Request: {}","/api/v1/get-university/"+ id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}