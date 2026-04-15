package com.unihub.classroom.assginement.service;


import com.unihub.classroom.assginement.dto.response.SubmissionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ISubmissionService {

    SubmissionResponseDto submitAssignment(UUID assignmentID, HttpServletRequest request, List<MultipartFile> submissionFiles) throws IOException;

    SubmissionResponseDto editSubmission(UUID sid,List<MultipartFile> files, List<String> toDeleteFiles,HttpServletRequest request) throws IOException;

    void deleteSubmission(UUID sid, HttpServletRequest request);

    SubmissionResponseDto getStudentSubmission(UUID aid, HttpServletRequest request);

    List<SubmissionResponseDto> getSubmissions(UUID aid, HttpServletRequest request, int pageNum);

    SubmissionResponseDto getInstructorSubmission(UUID sid, HttpServletRequest request);

    //TODO ADD INSTRUCTOR ASSIGN POINTS
}
