package com.unihub.universitymanagement.universitymanagement.internal.client;

import com.unihub.universitymanagement.universitymanagement.internal.client.fallback.AuthFeignClientFallBack;
import com.unihub.universitymanagement.universitymanagement.internal.dto.request.SystemAdminRequest;
import com.unihub.universitymanagement.universitymanagement.internal.dto.response.SystemAdminResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "auth",fallback = AuthFeignClientFallBack.class)
public interface AuthFeignClient {

    @PostMapping(value = "/api/v1/internal/user/create", consumes = "application/json")
    ResponseEntity<SystemAdminResponse> createSystemAdmin(@RequestBody SystemAdminRequest request ,@RequestHeader("X-API-KEY") String apiKey);

    @GetMapping(value = "/api/v1/internal/user/get-users-count/{cid}", consumes = "application/json")
    ResponseEntity<Long> countUsers(@PathVariable UUID cid , @RequestHeader("X-API-KEY") String apiKey);

}
