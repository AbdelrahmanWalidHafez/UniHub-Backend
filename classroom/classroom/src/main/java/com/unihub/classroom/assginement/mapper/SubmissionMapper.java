package com.unihub.classroom.assginement.mapper;

import com.unihub.classroom.assginement.dto.response.SubmissionResponseDto;
import com.unihub.classroom.assginement.model.Submission;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public SubmissionResponseDto toDto(Submission submission){
        SubmissionResponseDto submissionResponseDto = new SubmissionResponseDto();
        submissionResponseDto.setSid(submission.getSid());
        submissionResponseDto.setSubmissionUrls(submission.getSubmissionUrls());
        submissionResponseDto.setCreatedAt(submission.getCreatedAt());
        submissionResponseDto.setUpdatedAt(submission.getUpdatedAt());
        submissionResponseDto.setCreatedBy(submission.getCreatedBy());
        submissionResponseDto.setUpdatedBy(submission.getUpdatedBy());
        return submissionResponseDto;
    }
}
