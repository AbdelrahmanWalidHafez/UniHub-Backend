package com.unihub.taskmanager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.taskmanager.model.Priority;
import com.unihub.taskmanager.model.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TaskDto {

    private UUID id;

    private String description;

    private String title;

    private Status status;

    private Priority priority;

    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    @JsonProperty("started_at")
    private LocalDateTime startedAt;

    @JsonProperty("finished_at")
    private LocalDateTime finishedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;

}
