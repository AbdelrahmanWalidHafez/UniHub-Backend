package com.unihub.universitymanagement.universitymanagement.subscription.service.impl;

import com.unihub.universitymanagement.universitymanagement.subscription.dto.request.SubscriptionPlan;
import com.unihub.universitymanagement.universitymanagement.subscription.model.UniversitySubscriptionPlan;
import com.unihub.universitymanagement.universitymanagement.subscription.service.ISubscriptionService;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import com.unihub.universitymanagement.universitymanagement.university.repository.UniversityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final UniversityRepository universityRepository;

    @Override
    public void update(SubscriptionPlan request) {
        University university=fetchUniversity(request.getUniversityID());
        UniversitySubscriptionPlan universitySubscriptionPlan=generatePlan(request);
        universitySubscriptionPlan.setUniversity(university);
        university.setSubscriptionPlan(universitySubscriptionPlan);
        universityRepository.save(university);
    }

    private University fetchUniversity(UUID universityId){
        return universityRepository.findById(universityId)
                .orElseThrow(()->new EntityNotFoundException("University with id "+universityId+" not found"));
    }

    private UniversitySubscriptionPlan generatePlan(SubscriptionPlan request) {
        return UniversitySubscriptionPlan.builder()
                .pid(request.getPid())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
