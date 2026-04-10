package com.unihub.classroom.clazz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassroomDto {

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

}
