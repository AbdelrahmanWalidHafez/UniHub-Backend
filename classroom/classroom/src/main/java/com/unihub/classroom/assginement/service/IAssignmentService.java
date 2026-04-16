package com.unihub.classroom.assginement.service;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.assginement.model.Assignment;
import com.unihub.classroom.material.model.Material;

public interface IAssignmentService {

    AssignmentResponseDto createAssignment(Material material, Assignment assignment);

    AssignmentResponseDto editAssignment(Material material, CreateAssignmentRequest request);
}
