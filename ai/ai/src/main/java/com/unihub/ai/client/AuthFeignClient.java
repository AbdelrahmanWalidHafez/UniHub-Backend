package com.unihub.ai.client;

import com.unihub.ai.client.dto.UserDto;
import com.unihub.ai.client.fallback.AuthFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth",fallback = AuthFallBack.class)
public interface AuthFeignClient {

    @GetMapping(value = "/api/v1/internal/user/user-info",consumes = "application/json")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader("X-User-Email") String email,@RequestHeader("X-API-KEY") String apiKey);
}
