package com.unihub.subscription.stripe.controller;

import com.unihub.subscription.common.exception.dto.ErrorResponseDto;
import com.unihub.subscription.stripe.dto.request.CreateSubscriptionPlanDto;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponses;
import com.unihub.subscription.stripe.mapper.SubscriptionPlanMapper;
import com.unihub.subscription.stripe.service.ISubscriptionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription-plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanMapper mapper;

    private final ISubscriptionPlanService subscriptionPlanService;

    @PostMapping("/customer-service/create")
    public ResponseEntity<SubscriptionPlanResponseDto> createSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanDto createSubscriptionPlanDto){
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(subscriptionPlanService.createSubscriptionPlan(createSubscriptionPlanDto));
    }

    @GetMapping("/customer-service/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> getSubscriptionPlan(@PathVariable UUID id){
        return ResponseEntity.ok(mapper.toDto(subscriptionPlanService.getPlan(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<SubscriptionPlanResponses> getSubscriptionPlan(
            @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "sort_dir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "sort_field", defaultValue = "createdAt") String sortField){
        return ResponseEntity.ok(
                SubscriptionPlanResponses.builder()
                        .plans(subscriptionPlanService.getAllSubscriptionPlans(pageNum,sortDir,sortField))
                        .build()
        );
    }

    @PutMapping("/customer-service/update/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> updateSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanDto createSubscriptionPlanDto,@PathVariable UUID id){
        return ResponseEntity.ok(subscriptionPlanService.updatePlan(createSubscriptionPlanDto,id));
    }

    @DeleteMapping("/customer-service/delete/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@PathVariable UUID id){
        subscriptionPlanService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/system-admin/set-university-subscription/{id}")
    public ResponseEntity<Void> setSubscriptionPlan(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionPlanService.setUniversityPlan(request,id));
    }

}
