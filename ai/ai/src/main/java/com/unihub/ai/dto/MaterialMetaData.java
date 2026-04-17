package com.unihub.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
