package com.unihub.universitymanagement.universitymanagement.internal.service;

import com.unihub.universitymanagement.universitymanagement.university.dto.request.CreateUniversityRequest;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;

import java.util.UUID;

public interface IInternalService {

    UniversityResponse createUniversity(CreateUniversityRequest request);

    Long findUniversitiesBySubscriptionPlanId(UUID subscriptionPlanId);

}
