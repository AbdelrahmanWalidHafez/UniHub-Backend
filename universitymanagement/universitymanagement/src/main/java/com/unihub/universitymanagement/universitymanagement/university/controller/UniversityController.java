package com.unihub.universitymanagement.universitymanagement.university.controller;

import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetadataResponses;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import com.unihub.universitymanagement.universitymanagement.university.service.IUniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(
        name = "University API",
        description = "APIs for fetching universities"
)
public class UniversityController {

    private final IUniversityService universityService;

    @Operation(
            summary = "fetch universities metadata",
            description = "Enables a Customer service user to fetch universities metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "universities fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UniversityMetadataResponses.class))
            ),
    })
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

    @Operation(
            summary = "fetch a university by its id",
            description = "Enables a Customer service user to fetch universities by its id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "university fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UniversityResponse.class))
            )
    })
    @GetMapping("/get-university/{id}")
    public ResponseEntity<UniversityResponse> searchUniversity(@PathVariable UUID id){
        return ResponseEntity.ok(universityService.getUniversity(id));
    }

    @Operation(
            summary = "searches a university ",
            description = "Enables a Customer service user to search for a university"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "universities with similar name fetched successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UniversityResponse.class))
            ),
    })
    @GetMapping("/customer-service/search-university")
    public ResponseEntity<UniversityMetadataResponses> searchUniversity(@RequestParam("search_text") String searchText){
        return ResponseEntity.ok(universityService.searchUniversity(searchText));
    }

}
