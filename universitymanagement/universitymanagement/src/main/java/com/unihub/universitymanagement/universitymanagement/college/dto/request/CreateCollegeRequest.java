package com.unihub.universitymanagement.universitymanagement.college.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateCollegeRequest {
    @Size(min = 2, max = 70, message = "College name must be between 2 and 70 characters")
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "college name contains invalid characters"
    )
    @JsonProperty("college_name")
    private String collegeName;

    @Size(min = 10, max = 80, message = "College campus must be between 10 and 80 characters")
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "College campus contains invalid characters")
    @JsonProperty("college_campus")
    private String collegeCampus;

}
