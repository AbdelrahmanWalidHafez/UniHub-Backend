package com.unihub.usage.client.fallback;

import com.unihub.usage.client.UniversityFeignClient;
import com.unihub.usage.dto.response.CollegeDashboardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class UniversityFallBack  implements UniversityFeignClient {
    @Override
    public ResponseEntity<List<CollegeDashboardDTO>> getCollegeAnalysis(UUID tid) {
        log.error("Feign client fallback triggered: University service is unavailable.");
        List<CollegeDashboardDTO>emptyDto=List.of();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(emptyDto);
    }
}
