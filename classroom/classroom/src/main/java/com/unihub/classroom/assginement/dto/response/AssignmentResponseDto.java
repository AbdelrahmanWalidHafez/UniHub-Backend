package com.unihub.classroom.assginement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.classroom.material.dto.response.MaterialResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseDto {

    MaterialResponseDto material;

    Integer points;

    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;


}
