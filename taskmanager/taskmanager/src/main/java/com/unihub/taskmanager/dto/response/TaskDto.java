package com.unihub.taskmanager.dto.response;

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
//todo use @jsonproperty to rename variable in the json response
public class TaskDto {

    private UUID id;

    private String description;

    private String title;

    private Status status;

    private Priority priority;

    private LocalDateTime dueDate;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}
