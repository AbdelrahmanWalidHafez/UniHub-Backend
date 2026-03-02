package com.unihub.subscription.stripe.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.unihub.subscription.stripe.config.StripeConfigurationProperties;
import com.unihub.subscription.stripe.dto.response.StripeResponse;
import com.unihub.subscription.stripe.model.SubscriptionPlan;
import com.unihub.subscription.stripe.service.IStripeService;
import com.unihub.subscription.stripe.service.ISubscriptionPlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements IStripeService {

    private final ISubscriptionPlanService subscriptionPlanService;

    private final StripeConfigurationProperties stripeConfigurationProperties;

    @Override
    public StripeResponse checkoutPlan(HttpServletRequest request, UUID id)  {
        SubscriptionPlan plan=subscriptionPlanService.getPlan(id);
        Customer customer;
        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(request.getHeader("X-User-Email"))
                    .build();
            customer = Customer.create(customerParams);
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe customer: " + e.getMessage(), e);
        }
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(plan.getCurrency())
                        .setUnitAmount(plan.getPrice()*100)
                        .setRecurring(
                                SessionCreateParams.LineItem.PriceData.Recurring.builder()
                                        .setInterval(
                                                SessionCreateParams.LineItem.PriceData.Recurring.Interval.YEAR
                                        )
                                        .build()
                        )
                        .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(plan.getPlanName())
                                        .setDescription(plan.getPlanDescription())
                                        .build()
                        )
                        .build();
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setSuccessUrl(stripeConfigurationProperties.successUrl())
                        .setCancelUrl(stripeConfigurationProperties.cancelUrl())
                        .setCustomer(customer.getId())
                        .addLineItem(lineItem)
                        .build();
        try {
            Session session = Session.create(params);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Subscription checkout session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            log.error("Failed to create Stripe checkout session",e);
            throw new RuntimeException("Failed to create Stripe checkout session");
        }
    }

}
