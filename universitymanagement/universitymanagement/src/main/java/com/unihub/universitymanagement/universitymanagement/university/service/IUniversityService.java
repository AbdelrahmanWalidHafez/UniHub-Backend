package com.unihub.universitymanagement.universitymanagement.university.service;

import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetaData;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetadataResponses;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;

import java.util.List;
import java.util.UUID;

public interface IUniversityService {

    List<UniversityMetaData> getUniversityMetaDataList(int pageNum, String sortDir, String sortField);

    UniversityResponse getUniversity(UUID universityId);

    UniversityMetadataResponses searchUniversity(String searchText);

}
