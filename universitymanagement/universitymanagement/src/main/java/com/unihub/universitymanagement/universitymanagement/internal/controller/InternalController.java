package com.unihub.universitymanagement.universitymanagement.internal.controller;

import com.unihub.universitymanagement.universitymanagement.internal.dto.response.CollegeDashboardDTO;
import com.unihub.universitymanagement.universitymanagement.internal.service.IInternalService;
import com.unihub.universitymanagement.universitymanagement.university.dto.request.CreateUniversityRequest;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal")
public class InternalController {

    private final IInternalService universityService;

    @PostMapping("/create")
    public ResponseEntity<UniversityResponse> createUniversity( @RequestBody CreateUniversityRequest request){
        return ResponseEntity.ok(universityService.createUniversity(request));
    }

    @GetMapping("/get-subscription-plan-count/{id}")
    public ResponseEntity<Long> getSubscriptionPlanCount(@PathVariable UUID id){
        return ResponseEntity.ok(universityService.findUniversitiesBySubscriptionPlanId(id));
    }
    @GetMapping("/college-analysis")
    public ResponseEntity<List<CollegeDashboardDTO>> getCollegeAnalysis(@RequestParam UUID tid){
        return ResponseEntity.ok(universityService.getCollegeDashBoard(tid));
    }
}
