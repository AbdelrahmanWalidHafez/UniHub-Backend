package com.unihub.ai.dto;

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

    private UUID universityId;

    private UUID collegeId;

}
