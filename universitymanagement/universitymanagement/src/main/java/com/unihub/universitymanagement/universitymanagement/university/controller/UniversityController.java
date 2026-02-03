package com.unihub.universitymanagement.universitymanagement.university.controller;

import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetadataResponses;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import com.unihub.universitymanagement.universitymanagement.university.service.IUniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UniversityController {

    private final IUniversityService universityService;

    @GetMapping("/customer-service/get-universities")
    public ResponseEntity<UniversityMetadataResponses> searchUniversity(
            @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField){
        return ResponseEntity.ok(UniversityMetadataResponses
                .builder()
                .universityMetaDataList(universityService.getUniversityMetaDataList(pageNum, sortDir, sortField))
                .build());
    }

    @GetMapping("/get-university/{id}")
    public ResponseEntity<UniversityResponse> searchUniversity(@PathVariable UUID id){
        return ResponseEntity.ok(universityService.getUniversity(id));
    }

    @GetMapping("/customer-service/search-university")
    public ResponseEntity<UniversityMetadataResponses> searchUniversity(@RequestParam("search_text") String searchText){
        return ResponseEntity.ok(universityService.searchUniversity(searchText));
    }

}
