package com.unihub.universitymanagement.universitymanagement.college.Controller;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeMetadataResponses;
import com.unihub.universitymanagement.universitymanagement.college.service.ICollegeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/colleges/system-admin")
public class CollegeController {

    private final ICollegeService collegeService;

    @PostMapping("/create")
    public ResponseEntity<CollegeDto> createCollege(@Valid @RequestBody CreateCollegeRequest collegeRequest, HttpServletRequest request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createCollege(collegeRequest,request));
    }

    @GetMapping("/get-college/{id}")
    public ResponseEntity<CollegeDto> getCollege(@PathVariable UUID id){
        return  ResponseEntity.ok(collegeService.getCollege(id));
    }

    /**
     * the purpose of the HttpRequest object injected as a method parameter to the controller is to fetch from it the university id header.
     * this will allow us to fetch the university id of the System admin user using this api which will lead to fetching the university that
     * he/she is allowed to see and act on and this will lead to multitenancy
     * @param  request the http servlet request coming from the api gateway
     * @author Abdelrahman walid
     * @since 2/23/26
     * @return CollegeMetaDataRResponses
     */
    @GetMapping("/get-colleges")
    public ResponseEntity<CollegeMetadataResponses> getAllColleges(
            HttpServletRequest request,
            @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField){
        return ResponseEntity.ok(
                CollegeMetadataResponses.builder()
                        .colleges(collegeService.getAllColleges(request,pageNum,sortDir,sortField))
                        .build()
        ) ;
    }

}
