package com.unihub.auth.internal.client;


import com.unihub.auth.internal.client.fallback.UniversityFallBack;
import com.unihub.auth.internal.dto.response.UniversityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "universitymanagement", fallback = UniversityFallBack.class)
public interface UniversityFeignClient {

    @GetMapping(value = "/api/v1/get-university/{id}", consumes = "application/json")
    ResponseEntity<UniversityResponse> fetchUniversity(@PathVariable UUID id);

}
