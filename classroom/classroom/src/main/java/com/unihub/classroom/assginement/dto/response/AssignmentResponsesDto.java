package com.unihub.classroom.assginement.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponsesDto {

    List<AssignmentResponseDto> assignments;
}
