package com.unihub.classroom.assginement.mapper;

import com.unihub.classroom.assginement.dto.request.CreateAssignmentRequest;
import com.unihub.classroom.assginement.dto.response.AssignmentResponseDto;
import com.unihub.classroom.assginement.model.Assignment;
import com.unihub.classroom.material.mapper.MaterialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentMapper {

    private final MaterialMapper materialMapper;

    public Assignment toEntity(CreateAssignmentRequest request) {
        return Assignment.builder()
                .points(request.getPoints())
                .dueDate(request.getDueDate())
                .build();
    }

    public AssignmentResponseDto toDto(Assignment assignment){
        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();
        assignmentResponseDto.setAid(assignment.getId());
        assignmentResponseDto.setMaterial(materialMapper.toDto(assignment.getMaterial()));
        assignmentResponseDto.setPoints(assignment.getPoints());
        assignmentResponseDto.setDueDate(assignment.getDueDate());
        return assignmentResponseDto;
    }
}
