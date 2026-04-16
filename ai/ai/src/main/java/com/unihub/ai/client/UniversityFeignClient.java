package com.unihub.ai.client;



import com.unihub.ai.client.dto.CollegeDto;
import com.unihub.ai.client.dto.UniversityResponse;
import com.unihub.ai.client.fallback.UniversityFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "universitymanagement", fallback = UniversityFallBack.class)
public interface UniversityFeignClient {

    @GetMapping(value = "/api/v1/get-university/{id}", consumes = "application/json")
    ResponseEntity<UniversityResponse> fetchUniversity(@PathVariable UUID id);


    @GetMapping(value = "/api/v1/internal/get-college/{id}/{tid}", consumes = "application/json")
    ResponseEntity<CollegeDto> getCollege(@PathVariable UUID id,@PathVariable UUID tid);

}
