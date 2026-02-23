package com.unihub.universitymanagement.universitymanagement.college.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeDto {

    @JsonProperty("college_id")
    private UUID id;

    @JsonProperty("college_name")
    private String collegeName;

    private String campus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedBy;
}
