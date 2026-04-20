package com.unihub.ai.client.fallback;

import com.unihub.ai.client.ClassRoomFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ClassRoomFallBack implements ClassRoomFeignClient {

    @Override
    public ResponseEntity<List<UUID>> getClasses(String email) {
        log.error("Feign client fallback triggered: classroom service is unavailable. Request: {}","/api/v1/internal/get-classes");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(List.of());
    }
}
