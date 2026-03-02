package com.unihub.usage.client;

import com.unihub.usage.client.fallback.AuthFallBack;
import com.unihub.usage.dto.response.DashBoardAggregatesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name="auth",fallback = AuthFallBack.class)
public interface AuthFeignClient {

    @GetMapping(value = "/api/v1/internal/user/analysis", consumes = "application/json")
    ResponseEntity<DashBoardAggregatesDto> getUserAnalysis(@RequestParam UUID tid , @RequestHeader("X-API-KEY") String apiKey);
}

