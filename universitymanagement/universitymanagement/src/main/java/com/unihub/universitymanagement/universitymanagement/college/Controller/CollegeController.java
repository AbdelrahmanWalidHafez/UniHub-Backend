package com.unihub.universitymanagement.universitymanagement.college.Controller;

import com.unihub.universitymanagement.universitymanagement.college.dto.request.CreateCollegeRequest;
import com.unihub.universitymanagement.universitymanagement.college.dto.response.CollegeDto;
import com.unihub.universitymanagement.universitymanagement.college.service.ICollegeService;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system-admin/colleges")
public class CollegeController {

    private final ICollegeService collegeService;

    @PostMapping("/create")
    public ResponseEntity<CollegeDto> createCollege(@Valid @RequestBody CreateCollegeRequest collegeRequest){
        return  ResponseEntity.status(HttpStatus.CREATED).body(collegeService.createCollege(collegeRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollegeDto> getCollege(@PathVariable UUID id){
        return  ResponseEntity.status(HttpStatus.OK).body(collegeService.getCollege(id));
    }



}
