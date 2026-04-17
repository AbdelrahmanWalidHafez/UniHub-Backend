package com.unihub.classroom.material.dto.request;


import com.unihub.classroom.material.model.enums.MaterialType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialMetaData {

    private String headLine;

    private String description;

    private MaterialType materialType;

    private UUID classroomId;

    private UUID materialId;

    private String classTitle;

    private String classSubTitle;

    private UUID collegeId;

    private UUID universityId;

}
