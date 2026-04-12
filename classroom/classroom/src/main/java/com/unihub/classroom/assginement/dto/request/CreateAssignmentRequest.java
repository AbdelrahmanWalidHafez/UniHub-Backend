package com.unihub.classroom.assginement.dto.request;

import com.unihub.classroom.material.dto.request.MaterialDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentRequest {

    private MaterialDto materialDto;

    private Integer points;
}
