package com.unihub.classroom.assginement.service.impl;

import com.unihub.classroom.assginement.dto.response.SubmissionResponseDto;
import com.unihub.classroom.assginement.mapper.SubmissionMapper;
import com.unihub.classroom.assginement.model.Assignment;
import com.unihub.classroom.assginement.model.Submission;
import com.unihub.classroom.assginement.repository.AssignmentRepository;
import com.unihub.classroom.assginement.repository.SubmissionRepository;
import com.unihub.classroom.assginement.service.ISubmissionService;
import com.unihub.classroom.utils.FileUtils;
import com.unihub.classroom.utils.HttpHeadersUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements ISubmissionService {


    private final FileUtils fileUtils;

    private final HttpHeadersUtils httpHeadersUtils;

    private final SubmissionMapper submissionMapper;

    private final AssignmentRepository assignmentRepository;

    private final SubmissionRepository submissionRepository;

    @Override
    @Transactional
    public SubmissionResponseDto submitAssignment(UUID assignmentID, HttpServletRequest request, List<MultipartFile> submissionFiles) throws IOException {
        Assignment assignment=fetchAssignment(assignmentID,request);
        checkDueDate(assignment);
        Submission submission=new Submission();
        submission.setEdited(false);
        submission.setGrade(-1);
        assignment.addSubmission(submission);
        if(submissionFiles!=null&&!submissionFiles.isEmpty()){
            //TODO UPLOAD THE FILES IN VECTOR DB AT AI MS;
            fileUtils.uploadFiles(submissionFiles,assignment.getMaterial().getClassroom(), submission, Submission::getSubmissionUrls);
        }
        return submissionMapper.toDto(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public SubmissionResponseDto editSubmission(UUID sid, List<MultipartFile> files, List<String> toDeleteFiles, HttpServletRequest request) throws IOException {
        Submission submission=fetchStudentSubmission(sid,request);
         checkDueDate(submission.getAssignment());
        submission.setEdited(true);
        if(files!=null&&!files.isEmpty()){
            fileUtils.uploadFiles(files,submission.getAssignment().getMaterial().getClassroom(), submission, Submission::getSubmissionUrls);
        }
        if(toDeleteFiles!=null&&!toDeleteFiles.isEmpty()){
            toDeleteFiles.forEach(fileUtils::deleteFile);
            submission.getSubmissionUrls().removeAll(toDeleteFiles);
        }
        return submissionMapper.toDto(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public void deleteSubmission(UUID sid, HttpServletRequest request) {
        Submission submission=fetchForDeletion(sid,request);
        Assignment assignment=submission.getAssignment();
        checkDueDate(assignment);
        if (submission.getSubmissionUrls() != null) {
            submission.getSubmissionUrls().forEach(fileUtils::deleteFile);
        }
        assignment.removeSubmission(submission);
        submissionRepository.delete(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionResponseDto getStudentSubmission( UUID aid, HttpServletRequest request){
        return submissionMapper.toDto(fetchStudentSubmissionByAssignment(aid,request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDto> getSubmissions(UUID aid, HttpServletRequest request, int pageNum) {
        return submissionRepository.findAllByAssignment(aid, httpHeadersUtils.fetchEmailFromHeader(request),generatePageable(pageNum))
                .stream()
                .map(submissionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionResponseDto getInstructorSubmission(UUID sid, HttpServletRequest request) {
        return submissionMapper.toDto(fetchInstructorSubmission(sid,request));
    }

    @Override
    public SubmissionResponseDto gradeSubmission(UUID sid, HttpServletRequest request,Integer grade) {
        Submission submission=fetchInstructorSubmission(sid,request);
        if(submission.getAssignment().getPoints()<grade){
            throw new IllegalArgumentException("Grade cannot be higher than the assignment points");
        }
        submission.setGrade(grade);
        return submissionMapper.toDto(submissionRepository.save(submission));
    }


    private Assignment fetchAssignment(UUID assignmentID, HttpServletRequest request){
        return assignmentRepository.findAssignment(assignmentID, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Assignment not found with id: "+assignmentID));
    }

    private Submission fetchStudentSubmission(UUID sid,HttpServletRequest request){
          return submissionRepository.findBySidAndCreatedBy(sid, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Submission not found with id: "+sid));
    }

    private Submission fetchForDeletion(UUID sid,HttpServletRequest request){
        return submissionRepository.findByIdForDeletion(sid, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Submission not found with id: "+sid));
    }

    private Submission fetchStudentSubmissionByAssignment(UUID aid, HttpServletRequest request){
        return submissionRepository.findByCreatedByAndAssignment_Id(httpHeadersUtils.fetchEmailFromHeader(request),aid)
                .orElseThrow(()->new EntityNotFoundException("assignment not found with id: "+aid));
    }

    private Submission fetchInstructorSubmission(UUID sid, HttpServletRequest request){
        return submissionRepository
                .findSubmission(sid, httpHeadersUtils.fetchEmailFromHeader(request))
                .orElseThrow(()->new EntityNotFoundException("Submission not found with id: "+sid));
    }


    private Pageable generatePageable(int pageNum){
        return PageRequest.of(pageNum-1,5, Sort.by("createdAt").descending());
    }

    private void checkDueDate(Assignment assignment){
        if (assignment.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Assignment deadline has passed");
        }
    }

}
