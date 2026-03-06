package com.unihub.announcement.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.announcement.post.model.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @JsonProperty("post_id")
    private UUID id;

    private String title;

    private String content;

    private String media;

    private Status status;

    @JsonProperty("college_id")
    private UUID cid;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    private String updatedBy;
}
