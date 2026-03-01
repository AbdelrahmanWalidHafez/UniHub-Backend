package com.unihub.auth.accountmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUserRequest  extends  BaseUserRequest{

    @JsonProperty("college_id")
    @NotNull(message = "CID is required")
    private UUID cid;

    @NotNull(message = "GPA is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "GPA cannot be less than 0.0")
    @DecimalMax(value = "4.0", inclusive = true, message = "GPA cannot be greater than 4.0")
    private Double gpa;
}
