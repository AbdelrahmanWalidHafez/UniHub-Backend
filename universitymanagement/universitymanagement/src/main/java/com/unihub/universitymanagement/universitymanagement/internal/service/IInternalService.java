package com.unihub.universitymanagement.universitymanagement.internal.service;

import com.unihub.universitymanagement.universitymanagement.internal.dto.response.CollegeDashboardDTO;
import com.unihub.universitymanagement.universitymanagement.university.dto.request.CreateUniversityRequest;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;

import java.util.List;
import java.util.UUID;

public interface IInternalService {

    UniversityResponse createUniversity(CreateUniversityRequest request);

    Long findUniversitiesBySubscriptionPlanId(UUID subscriptionPlanId);

    List<CollegeDashboardDTO> getCollegeDashBoard(UUID tid);

}
