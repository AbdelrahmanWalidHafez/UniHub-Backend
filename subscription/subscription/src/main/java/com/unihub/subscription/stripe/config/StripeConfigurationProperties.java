package com.unihub.subscription.stripe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeConfigurationProperties(String stripeSk,String stripePk,String successUrl,String cancelUrl) {
}
