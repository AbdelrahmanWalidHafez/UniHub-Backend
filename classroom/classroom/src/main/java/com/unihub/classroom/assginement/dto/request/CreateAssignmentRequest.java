package com.unihub.classroom.assginement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.classroom.material.dto.request.MaterialDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentRequest {

    @JsonProperty("material")
    @NotNull(message = "Material cannot be blank")
    private MaterialDto materialDto;

    @NotNull(message = "Point cannot be blank")
    @Min(value = 1, message = "Points cannot be less than 1")
    @Max(value = 100, message = "Points cannot exceed 1000")
    private Integer points;

    @JsonProperty("due_date")
    @NotNull(message = "Due date cannot be blank")
    @FutureOrPresent(message = "Due date must be in the future")
    private LocalDateTime dueDate;
}
