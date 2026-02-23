package com.unihub.universitymanagement.universitymanagement.college.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CollegeMetadataResponses {

   @JsonProperty("colleges")
    List<CollegeMetadata> colleges;
}
