package com.unihub.classroom.assginement.service.impl;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.assginement.mapper.AssignmentMapper;
import com.unihub.classroom.assginement.model.Assignment;
import com.unihub.classroom.assginement.repository.AssignmentRepository;
import com.unihub.classroom.assginement.service.IAssignmentService;
import com.unihub.classroom.material.model.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService {

    private final AssignmentMapper assignmentMapper;

    private final AssignmentRepository assignmentRepository;

    @Override
    public AssignmentResponseDto createAssignment(Material material, Assignment assignment)  {
        assignment.setMaterial(material);
        assignmentRepository.save(assignment);
        return assignmentMapper.toDto(assignment);
    }

    @Override
    public AssignmentResponseDto editAssignment(Material material,CreateAssignmentRequest request) {
        Assignment assignment=material.getAssignment();
        assignment.setPoints(request.getPoints());
        return assignmentMapper.toDto(assignmentRepository.save(assignment));
    }

}
