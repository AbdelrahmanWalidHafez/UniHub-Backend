package com.unihub.universitymanagement.universitymanagement.university.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;


@Builder
@AllArgsConstructor
public class UniversityMetadataResponses {

    @JsonProperty("universities")
    List<UniversityMetaData> universityMetaDataList;
}
