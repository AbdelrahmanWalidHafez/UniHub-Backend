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
@Tag(
        name = "Subscription Plan API",
        description = "APIs for managing subscription plans, including creation, retrieval, update, deletion, and assigning plans to universities"
)
public class SubscriptionPlanController {

    private final SubscriptionPlanMapper mapper;

    private final ISubscriptionPlanService subscriptionPlanService;

    @Operation(
            summary = "Creates a new Subscription Plan",
            description = "Enables a Customer service user to create a subscription plan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Subscription plan created successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionPlanResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "there is an existing subscription plan with the same name",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/customer-service/create")
    public ResponseEntity<SubscriptionPlanResponseDto> createSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanDto createSubscriptionPlanDto){
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(subscriptionPlanService.createSubscriptionPlan(createSubscriptionPlanDto));
    }

    @Operation(
            summary = "get a  Subscription Plan by its id",
            description = "Enables a Customer service user to get a subscription plan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription plan retrieved",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionPlanResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription plan with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/customer-service/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> getSubscriptionPlan(@PathVariable UUID id){
        return ResponseEntity.ok(mapper.toDto(subscriptionPlanService.getPlan(id)));
    }

    @Operation(
            summary = "fetches all  Subscription Plans",
            description = "Enables a user to fetch all the available subscription plans"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription plans retrieved successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionPlanResponses.class))
            ),
    })
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

    @Operation(
            summary = "updates a Subscription Plan",
            description = "Enables a Customer service user to update a subscription plan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription plan updated successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionPlanResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription plan with the same name",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PutMapping("/customer-service/update/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> updateSubscriptionPlan(@Valid @RequestBody CreateSubscriptionPlanDto createSubscriptionPlanDto,@PathVariable UUID id){
        return ResponseEntity.ok(subscriptionPlanService.updatePlan(createSubscriptionPlanDto,id));
    }

    @Operation(
            summary = "Deletes a Subscription Plan",
            description = "Enables a Customer service user to delete a subscription plan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Subscription plan deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "cannot delete a plan",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription plan with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @DeleteMapping("/customer-service/delete/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@PathVariable UUID id){
        subscriptionPlanService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "set a Subscription Plan to a university",
            description = "Enables a System Admin user to set a subscription plan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription plan set successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = SubscriptionPlanResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "cannot set a plan",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription plan with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PostMapping("/system-admin/set-university-subscription/{id}")
    public ResponseEntity<Void> setSubscriptionPlan(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionPlanService.setUniversityPlan(request,id));
    }

    @PutMapping("/system-admin/upgrade-university-subscription/{id}")
    public ResponseEntity<Void> upgradeSubscriptionPlan(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionPlanService.upgradeUniversityPlan(request,id));
    }

}
