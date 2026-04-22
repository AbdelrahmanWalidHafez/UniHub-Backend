package com.unihub.universitymanagement.universitymanagement.internal.client.fallback;

import com.unihub.universitymanagement.universitymanagement.internal.client.AuthFeignClient;
import com.unihub.universitymanagement.universitymanagement.internal.dto.request.SystemAdminRequest;
import com.unihub.universitymanagement.universitymanagement.internal.dto.response.SystemAdminResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class AuthFeignClientFallBack implements AuthFeignClient {

    @Override
    public ResponseEntity<SystemAdminResponse> createSystemAdmin(SystemAdminRequest request, String apiKey) {
        log.error("Feign client fallback triggered: Auth service is unavailable. Request: {}", request);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<Long> countUsers(UUID cid, String apiKey) {
        log.error("Feign client fallback triggered: Auth service is unavailable.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}