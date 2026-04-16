package com.unihub.classroom.assginement.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetGradeRequest {

    @NotNull(message = "Grade cannot be blank")
    @Min(value = 0, message = "Grade cannot be less than 1")
    @Max(value = 100, message = "Grade cannot exceed 1000")
    private Integer grade;
}
