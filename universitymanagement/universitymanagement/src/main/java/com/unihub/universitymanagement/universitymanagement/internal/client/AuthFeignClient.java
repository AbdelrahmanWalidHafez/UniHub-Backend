package com.unihub.universitymanagement.universitymanagement.internal.client;

import com.unihub.universitymanagement.universitymanagement.internal.dto.request.SystemAdminRequest;
import com.unihub.universitymanagement.universitymanagement.internal.dto.response.SystemAdminResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth")
public interface AuthFeignClient {

    @PostMapping(value = "/api/v1/internal/user/create", consumes = "application/json")
    ResponseEntity<SystemAdminResponse> createSystemAdmin(@RequestBody SystemAdminRequest request ,@RequestHeader("X-API-KEY") String apiKey);

}
