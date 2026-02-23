package com.unihub.subscription.stripe.controller;

import com.unihub.subscription.stripe.dto.response.StripeResponse;
import com.unihub.subscription.stripe.service.IStripeService;
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
public class StripeController {

    private final IStripeService stripeService;

    @PostMapping("/create-session/{id}")
    public ResponseEntity<StripeResponse> createSession(HttpServletRequest request, @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(stripeService.checkoutPlan(request,id));
    }

}
