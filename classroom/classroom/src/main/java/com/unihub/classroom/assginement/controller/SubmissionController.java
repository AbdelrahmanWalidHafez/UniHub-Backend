package com.unihub.classroom.assginement.controller;

import com.unihub.classroom.assginement.dto.response.SubmissionResponseDto;
import com.unihub.classroom.assginement.dto.response.SubmissionResponsesDto;
import com.unihub.classroom.assginement.service.ISubmissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private final ISubmissionService submissionService;

    @PostMapping(value = "/student/submit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubmissionResponseDto> createAnnouncement(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                    @PathVariable UUID id,
                                                                    HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionService.submitAssignment(id, request, files));

    }

    @PutMapping(value = "/student/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubmissionResponseDto> editSubmission(@PathVariable UUID id,
                                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                @RequestPart(value = "ToDeleteFiles", required = false) List<String> toDeleteFiles,
                                                                HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(submissionService.editSubmission(id, files, toDeleteFiles, request));
    }

    @DeleteMapping("/student/delete/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable UUID id, HttpServletRequest request){
        submissionService.deleteSubmission(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/get-submission/{id}")
    public ResponseEntity<SubmissionResponseDto> getSubmission(@PathVariable UUID id, HttpServletRequest request){
        return ResponseEntity.ok(submissionService.getSubmission(id, request));
    }

    @GetMapping("/instructor/get-all-submissions/{id}")
    public ResponseEntity<SubmissionResponsesDto> getAllSubmissions(@PathVariable UUID id, HttpServletRequest request, @RequestParam(value = "page_num",defaultValue = "1")int pageNum){
        return ResponseEntity.ok(SubmissionResponsesDto.builder().submissions(submissionService.getSubmissions(id, request, pageNum)).build());
    }

    @GetMapping("/instructor/{aid}/get-submission/{sid}")
    public ResponseEntity<SubmissionResponseDto> getSubmission(@PathVariable UUID aid, @PathVariable UUID sid, HttpServletRequest request){
        return ResponseEntity.ok(submissionService.getSubmission(sid, aid, request));
    }

}
