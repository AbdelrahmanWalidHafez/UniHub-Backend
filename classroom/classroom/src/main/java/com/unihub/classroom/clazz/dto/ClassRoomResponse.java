package com.unihub.classroom.clazz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomResponse {

   @JsonProperty("class_id")
    private UUID id;

    @JsonProperty("entry_code")
    private String code;

    @JsonProperty("class_title")
    private String classTitle;

    @JsonProperty("class_sub_title")
    private String classSubTitle;

    @JsonProperty("image_num")
    private int imageNum;

    @JsonProperty("is_archived")
    private boolean isArchived;

    @JsonProperty("university_id")
    private UUID universityId;

    @JsonProperty("college_id")
    private UUID collegeId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;


}
