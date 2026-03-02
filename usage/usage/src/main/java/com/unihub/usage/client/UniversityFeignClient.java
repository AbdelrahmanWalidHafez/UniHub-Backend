package com.unihub.usage.client;

import com.unihub.usage.client.fallback.UniversityFallBack;
import com.unihub.usage.dto.response.CollegeDashboardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@FeignClient(name = "universitymanagement", fallback = UniversityFallBack.class)
public interface UniversityFeignClient {

    @GetMapping(value = "/api/v1/internal/college-analysis", consumes = "application/json")
    ResponseEntity<List<CollegeDashboardDTO>> getCollegeAnalysis(@RequestParam UUID tid);

}
