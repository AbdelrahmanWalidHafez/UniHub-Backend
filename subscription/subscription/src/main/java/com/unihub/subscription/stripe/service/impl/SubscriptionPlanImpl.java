package com.unihub.subscription.stripe.service.impl;

import com.unihub.subscription.stripe.dto.request.CreateSubscriptionPlanDto;
import com.unihub.subscription.stripe.dto.request.SubscriptionPlanRequest;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.stripe.mapper.SubscriptionPlanMapper;
import com.unihub.subscription.stripe.model.SubscriptionPlan;
import com.unihub.subscription.stripe.repository.SubscriptionPlanRepository;
import com.unihub.subscription.stripe.service.ISubscriptionPlanService;
import com.unihub.subscription.subscriptionrequest.client.UniversityFeignClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanImpl implements ISubscriptionPlanService {

    private final StreamBridge streamBridge;

    private final SubscriptionPlanMapper mapper;

    private final SubscriptionPlanRepository repository;

    private final UniversityFeignClient universityFeignClient;

    @Override
    @Transactional
    public SubscriptionPlanResponseDto createSubscriptionPlan(CreateSubscriptionPlanDto createSubscriptionPlanDto){
        SubscriptionPlan subscriptionPlan=mapper.toEntity(createSubscriptionPlanDto);
        subscriptionPlan=repository.save(subscriptionPlan);
        return mapper.toDto(subscriptionPlan);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponseDto updatePlan(CreateSubscriptionPlanDto createSubscriptionPlanDto,UUID id){
        SubscriptionPlan subscriptionPlan=fetchPlan(id);
        validatePlanCount(id);
        if(!createSubscriptionPlanDto.getPlanName().equalsIgnoreCase(subscriptionPlan.getPlanName())){
            subscriptionPlan.setPlanName(createSubscriptionPlanDto.getPlanName());
        }
        subscriptionPlan.setPlanDescription(createSubscriptionPlanDto.getPlanDescription());
        subscriptionPlan.setPrice(createSubscriptionPlanDto.getPrice());
        subscriptionPlan.setMaxUserAmount(createSubscriptionPlanDto.getMaxUserAmount());
        subscriptionPlan=repository.save(subscriptionPlan);
        return mapper.toDto(subscriptionPlan);
    }

    @Override
    public SubscriptionPlan getPlan(UUID id){
        return fetchPlan(id);
    }

    @Override
    public List<SubscriptionPlanResponseDto> getAllSubscriptionPlans(int pageNum, String sortDir,String sortField){
        int pageSize=3;
        Pageable pageable= PageRequest.of(
                pageNum-1,
                pageSize,
                sortDir.equalsIgnoreCase("asc")? Sort.by(sortField).ascending():Sort.by(sortField).descending()
        );
        return repository.findAll(pageable).stream().map(mapper::toDto).toList();
    }

    @Override
    public Void setUniversityPlan(HttpServletRequest request, UUID subscriptionPlanId) {
        UUID universityId=UUID.fromString(request.getHeader("X-User-University-Id"));
        SubscriptionPlanRequest subscriptionPlanRequest=generateSubscriptionPlanRequest(universityId,subscriptionPlanId);
        streamBridge.send("setUniversitySubscriptionPlan-out-0",subscriptionPlanRequest);
        return null;
    }

    @Override
    public Void upgradeUniversityPlan(HttpServletRequest request, UUID subscriptionPlanId) {
        UUID universityId=UUID.fromString(request.getHeader("X-User-University-Id"));
        SubscriptionPlanRequest subscriptionPlanRequest=generateSubscriptionPlanRequest(universityId,subscriptionPlanId);
        streamBridge.send("upgradeUniversitySubscriptionPlan-out-0",subscriptionPlanRequest);
        return null;
    }

    @Override
    public void deleteSubscription(UUID id) {
        validatePlanCount(id);
        repository.deleteById(id);
    }

    private SubscriptionPlan fetchPlan(UUID id){
        return  repository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("Subscription Request not found"));

    }

    private SubscriptionPlanRequest generateSubscriptionPlanRequest(UUID universityId, UUID subscriptionPlanId){
        return SubscriptionPlanRequest.builder()
                .pid(subscriptionPlanId)
                .startDate(LocalDate.now())
                .endDate(calculateEndDate(LocalDate.now()))
                .universityID(universityId)
                .build();
    }

    private LocalDate calculateEndDate(LocalDate date){
        return date.plusYears(1);
    }

    private void validatePlanCount(UUID id){
        Long planCount= universityFeignClient.getSubscriptionPlanCount(id).getBody();
        if(planCount>0){
            throw new IllegalArgumentException("There are "+planCount+" universities associated with this plan");
        }
    }
}
