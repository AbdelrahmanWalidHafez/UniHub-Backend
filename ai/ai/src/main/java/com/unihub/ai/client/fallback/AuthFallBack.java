package com.unihub.ai.client.fallback;

import com.unihub.ai.client.AuthFeignClient;
import com.unihub.ai.client.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFallBack implements AuthFeignClient {

    @Override
    public ResponseEntity<UserDto> getUserInfo(String email, String apiKey) {
        log.error("Feign client fallback triggered: AUTH service is unavailable. Request: {}","/api/v1/internal/user-info/"+email );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new UserDto());
    }
}
