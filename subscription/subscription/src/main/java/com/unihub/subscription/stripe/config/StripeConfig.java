package com.unihub.subscription.stripe.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StripeConfig {
    
    private final StripeConfigurationProperties stripeConfigurationProperties;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeConfigurationProperties.stripeSk();
    }
}