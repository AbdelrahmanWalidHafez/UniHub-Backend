package com.unihub.ai.client;

import com.unihub.ai.client.fallback.ClassRoomFallBack;
import com.unihub.ai.client.fallback.UniversityFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "classroom", fallback = ClassRoomFallBack.class)
public interface ClassRoomFeignClient {
    @GetMapping("/api/v1/internal/get-classes")
     ResponseEntity<List<UUID>> getClasses(@RequestHeader("X-User-Email") String email);
}
