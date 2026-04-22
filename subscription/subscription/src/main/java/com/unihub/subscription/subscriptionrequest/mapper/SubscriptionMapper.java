package com.unihub.subscription.subscriptionrequest.mapper;

import com.unihub.subscription.subscriptionrequest.dto.request.CreateUniversityRequest;
import com.unihub.subscription.subscriptionrequest.dto.request.SubscriptionRequestDto;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionRequestResponseDto;
import com.unihub.subscription.subscriptionrequest.dto.response.SubscriptionsMetaData;
import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public SubscriptionRequest toEntity(SubscriptionRequestDto dto) {
        SubscriptionRequest entity = new SubscriptionRequest();
        entity.setUniversityName(dto.getUniversityName());
        entity.setUniversityEmail(dto.getUniversityEmail());
        entity.setCountry(dto.getCountry());
        entity.setCity(dto.getCity());
        entity.setContactNumber(dto.getContactNumber());
        entity.setUniversityWebsiteUrl(dto.getUniversityWebsiteUrl());
        entity.setUniversityDomain(dto.getUniversityDomain());
        entity.setStatus(Status.PENDING);
        return entity;
    }

    public SubscriptionRequestResponseDto toDto(SubscriptionRequest entity) {
        SubscriptionRequestResponseDto dto = new SubscriptionRequestResponseDto();
        dto.setUniversityName(entity.getUniversityName());
        dto.setUniversityEmail(entity.getUniversityEmail());
        dto.setCity(entity.getCity());
        dto.setCountry(entity.getCountry());
        dto.setContactNumber(entity.getContactNumber());
        dto.setRid(entity.getRid());
        dto.setUniversityWebsiteUrl(entity.getUniversityWebsiteUrl());
        dto.setUniversityDomain(entity.getUniversityDomain());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setAccreditationKey(entity.getAccreditationKey());
        dto.setUniversityLogo(entity.getUniversityLogoKey());
        return dto;
    }


    public SubscriptionsMetaData toMetaData(SubscriptionRequest entity){
        SubscriptionsMetaData metaData=new SubscriptionsMetaData();
        metaData.setRid(entity.getRid());
        metaData.setUniversityName(entity.getUniversityName());
        metaData.setCreatedAt(entity.getCreatedAt());
        metaData.setCreatedBy(entity.getCreatedBy());
        metaData.setStatus(entity.getStatus());
        return metaData;
    }

    public CreateUniversityRequest toUniversityRequestDto(SubscriptionRequest entity){
        CreateUniversityRequest request=new CreateUniversityRequest();
        request.setUniversityName(entity.getUniversityName());
        request.setUniversityEmail(entity.getUniversityEmail());
        request.setCountry(entity.getCountry());
        request.setCity(entity.getCity());
        request.setContactNumber(entity.getContactNumber());
        request.setUniversityWebsiteUrl(entity.getUniversityWebsiteUrl());
        request.setUniversityDomain(entity.getUniversityDomain());
        request.setAccreditationKey(entity.getAccreditationKey());
        request.setLogoKey(entity.getUniversityLogoKey());
        return request;
    }
}
