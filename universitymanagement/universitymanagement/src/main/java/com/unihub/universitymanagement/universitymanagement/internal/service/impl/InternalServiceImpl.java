package com.unihub.universitymanagement.universitymanagement.internal.service.impl;

import com.unihub.universitymanagement.universitymanagement.college.repository.CollegeRepository;
import com.unihub.universitymanagement.universitymanagement.internal.client.AuthFeignClient;
import com.unihub.universitymanagement.universitymanagement.internal.dto.request.SystemAdminRequest;
import com.unihub.universitymanagement.universitymanagement.internal.dto.response.CollegeDashboardDTO;
import com.unihub.universitymanagement.universitymanagement.internal.dto.response.SystemAdminResponse;
import com.unihub.universitymanagement.universitymanagement.internal.service.IInternalService;
import com.unihub.universitymanagement.universitymanagement.university.dto.request.CreateUniversityRequest;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import com.unihub.universitymanagement.universitymanagement.university.mapper.UniversityMapper;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import com.unihub.universitymanagement.universitymanagement.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalServiceImpl implements IInternalService {

    @Value("${api.key}")
    private String apiKey;

    private final UniversityMapper universityMapper;

    private final AuthFeignClient authFeignClient;

    private final CollegeRepository collegeRepository;

    private final UniversityRepository universityRepository;

    @Override
    @Transactional
    public UniversityResponse createUniversity(CreateUniversityRequest request) {
        University university=universityMapper.toEntity(request);
        universityRepository.save(university);
        SystemAdminResponse systemAdminResponse=createSystemAdmin(university);
        return universityMapper.toDto(university,systemAdminResponse);
    }

    @Override
    public Long findUniversitiesBySubscriptionPlanId(UUID subscriptionPlanId) {
        return universityRepository.countBySubscriptionPlan_Pid(subscriptionPlanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeDashboardDTO> getCollegeDashBoard(UUID tid) {
        return collegeRepository.getCollegeDashboardData(tid);
    }

    private SystemAdminResponse createSystemAdmin(University university) {
        SystemAdminRequest request=SystemAdminRequest.builder()
                .tid(university.getUniId())
                .universityDomain(university.getUniversityDomain())
                .build();
        return handleResponse(request);
    }

    private SystemAdminResponse handleResponse(SystemAdminRequest request) {
        ResponseEntity<SystemAdminResponse> systemAdminResponseResponseEntity=authFeignClient.createSystemAdmin(request,apiKey);
        if (systemAdminResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            return  systemAdminResponseResponseEntity.getBody();
        }else{
            throw new RuntimeException("could not create system admin service might not be available");
        }

    }

}
