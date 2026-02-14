package com.unihub.universitymanagement.universitymanagement.university.mapper;

import com.unihub.universitymanagement.universitymanagement.internal.dto.response.SystemAdminResponse;
import com.unihub.universitymanagement.universitymanagement.subscription.mapper.UniversitySubscriptionPlanMapper;
import com.unihub.universitymanagement.universitymanagement.university.dto.request.CreateUniversityRequest;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityMetaData;
import com.unihub.universitymanagement.universitymanagement.university.dto.response.UniversityResponse;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniversityMapper {

    private final UniversitySubscriptionPlanMapper  universitySubscriptionPlanMapper;

    public University toEntity(CreateUniversityRequest request){
        University university = new University();
        university.setUniversityName(request.getUniversityName());
        university.setUniversityEmail(request.getUniversityEmail());
        university.setCountry(request.getCountry());
        university.setCity(request.getCity());
        university.setContactNumber(request.getContactNumber());
        university.setUniversityWebsiteUrl(request.getUniversityWebsiteUrl());
        university.setUniversityLogoKey(request.getLogoKey());
        university.setUniversityDomain(request.getUniversityDomain());
        university.setAccreditationKey(request.getAccreditationKey());
        return university;
    }

    public UniversityResponse toDto(University university, SystemAdminResponse systemAdminResponse){
        UniversityResponse universityResponse = toDto(university);
        universityResponse.setSystemAdmin(systemAdminResponse);
        return universityResponse;
    }

    public UniversityResponse toDto(University university){
        UniversityResponse universityResponse = new UniversityResponse();
        universityResponse.setUniId(university.getUniId());
        universityResponse.setUniversityName(university.getUniversityName());
        universityResponse.setUniversityEmail(university.getUniversityEmail());
        universityResponse.setCountry(university.getCountry());
        universityResponse.setCity(university.getCity());
        universityResponse.setUniversityDomain(university.getUniversityDomain());
        universityResponse.setContactNumber(university.getContactNumber());
        universityResponse.setUniversityWebsiteUrl(university.getUniversityWebsiteUrl());
        universityResponse.setUniversityLogo(university.getUniversityLogoKey());
        universityResponse.setAccreditationKey(university.getAccreditationKey());
        if (university.getSubscriptionPlan() != null) {
            universityResponse.setSubscriptionPlan(universitySubscriptionPlanMapper.toDto(university.getSubscriptionPlan()));
        }
        universityResponse.setCreatedAt(university.getCreatedAt());
        universityResponse.setUpdatedAt(university.getUpdatedAt());
        universityResponse.setCreatedBy(university.getCreatedBy());
        universityResponse.setUpdatedBy(university.getUpdatedBy());
        return universityResponse;
    }

    public UniversityMetaData toMetaData(University university){
        UniversityMetaData universityMetaData = new UniversityMetaData();
        universityMetaData.setUniId(university.getUniId());
        universityMetaData.setUniversityName(university.getUniversityName());
        universityMetaData.setCreatedAt(university.getCreatedAt());
        universityMetaData.setCreatedBy(university.getCreatedBy());
        universityMetaData.setUpdatedAt(university.getUpdatedAt());
        universityMetaData.setUpdatedBy(university.getUpdatedBy());
        return universityMetaData;
    }
}
