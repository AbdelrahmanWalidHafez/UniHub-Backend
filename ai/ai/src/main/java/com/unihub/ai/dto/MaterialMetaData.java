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

    @JsonProperty("head_line")
    @NotNull(message = "Headline must not be empty")
    @Size(min = 3, max = 100, message = "Headline must be between 3 and 100 characters")
    private String headLine;

    @NotNull(message = "Description must not be empty")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @JsonProperty("material_type")
    private MaterialType materialType;

    @JsonProperty("classroom_id")
    @NotNull(message = "Classroom id must not be empty")
    private UUID classroomId;

    @NotNull(message = "Class title is required")
    @Size(min = 2, max = 50, message = "Class title must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} .,'-]+$",
            message = "Must contain only letters, numbers, spaces and . , ' -"
    )
    @JsonProperty("class_title")
    private String classTitle;

    @NotNull(message = "Class subtitle is required")
    @Size(min = 2, max = 60, message = "Class subtitle must be between 2 and 60 characters")
    @Pattern(
            regexp = "^[\\p{L}\\p{N} .,'-]+$",
            message = "Must contain only letters, numbers, spaces and . , ' -"
    )
    @JsonProperty("class_sub_title")
    private String classSubTitle;

    @JsonProperty("university_id")
    @NotNull(message = "University id must not be empty")
    private UUID universityId;

    @JsonProperty("college_id")
    @NotNull(message = "College id must not be empty")
    private UUID collegeId;

}
