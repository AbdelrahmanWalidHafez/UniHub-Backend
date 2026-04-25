package com.unihub.taskmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.taskmanager.model.Priority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(min=1,max = 60, message = "Title must be between 1 and 60 characters")
    private String title;

    @Size(min=1,max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    @JsonProperty("due_date")
    private LocalDateTime dueDate;
}

