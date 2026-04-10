package com.unihub.classroom.clazz.dto;

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
public class MemberDto {

    @JsonProperty("record_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UUID rid;

    @JsonProperty("member_email")
    private String email;

    @JsonProperty("classroom_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String classroomId;

    @JsonProperty("created_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;

    @JsonProperty("updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedBy;
}
