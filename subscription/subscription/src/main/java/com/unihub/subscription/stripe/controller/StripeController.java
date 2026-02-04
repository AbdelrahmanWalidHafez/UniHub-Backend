package com.unihub.subscription.stripe.controller;

import com.unihub.subscription.common.exception.dto.ErrorResponseDto;
import com.unihub.subscription.stripe.dto.response.StripeResponse;
import com.unihub.subscription.stripe.dto.response.SubscriptionPlanResponseDto;
import com.unihub.subscription.stripe.service.IStripeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/system-admin/stripe")
@Tag(
        name = "Stripe API",
        description = "API for creating stripe sessions to enable payments"
)
public class StripeController {

    private final IStripeService stripeService;

    @Operation(
            summary = "creates stripe payment session",
            description = "Enables a system admin to create a stripe session of the chosen plan to check it out"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "session created successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = StripeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "bad request",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no subscription plan with the same id",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @PostMapping("/create-session/{id}")
    public ResponseEntity<StripeResponse> createSession(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(stripeService.checkoutPlan(request,id));
    }

}
