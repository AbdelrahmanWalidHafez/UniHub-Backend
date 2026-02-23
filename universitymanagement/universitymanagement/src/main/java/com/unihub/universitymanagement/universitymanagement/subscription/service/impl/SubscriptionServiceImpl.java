package com.unihub.universitymanagement.universitymanagement.subscription.service.impl;

import com.unihub.universitymanagement.universitymanagement.subscription.dto.request.SubscriptionPlan;
import com.unihub.universitymanagement.universitymanagement.subscription.model.UniversitySubscriptionPlan;
import com.unihub.universitymanagement.universitymanagement.subscription.service.ISubscriptionService;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import com.unihub.universitymanagement.universitymanagement.university.repository.UniversityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final UniversityRepository universityRepository;

    @Override
    @Transactional
    public void setPlan(SubscriptionPlan request) {
        University university=fetchUniversity(request.getUniversityID());
        UniversitySubscriptionPlan universitySubscriptionPlan=generatePlan(request);
        universitySubscriptionPlan.setUniversity(university);
        university.setSubscriptionPlan(universitySubscriptionPlan);
        universityRepository.save(university);
    }

    @Override
    @Transactional
    public void upgradePlan(SubscriptionPlan request){
        University university=fetchUniversity(request.getUniversityID());
        UniversitySubscriptionPlan universitySubscriptionPlan=university.getSubscriptionPlan();
        upgradePlan(universitySubscriptionPlan,request,university);
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

    private void  upgradePlan(UniversitySubscriptionPlan universitySubscriptionPlan,SubscriptionPlan subscriptionPlan,University university ){
        if(universitySubscriptionPlan==null){
            throw new EntityNotFoundException("No subscription plan found");
        }
        universitySubscriptionPlan.setPid(subscriptionPlan.getPid());
        universitySubscriptionPlan.setStartDate(subscriptionPlan.getStartDate());
        universitySubscriptionPlan.setEndDate(subscriptionPlan.getEndDate());
        universitySubscriptionPlan.setUniversity(university);
    }
}
