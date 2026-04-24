package com.unihub.taskmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.taskmanager.model.Priority;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//add validation
public class CreateTaskRequest {

    private String title;

    private String description;

    private Priority priority;

    @JsonProperty("due_date")
    private LocalDateTime dueDate;
}

