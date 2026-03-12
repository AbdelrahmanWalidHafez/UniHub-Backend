package com.unihub.universitymanagement.universitymanagement.internal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollegeDashboardDTO {

    @JsonProperty("total_colleges")
    private long totalColleges;

    @JsonProperty("college_id")
    private UUID collegeId;

    @JsonProperty("college_name")
    private String collegeName;
}