package com.unihub.subscription.state.impl;

import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;

@RequiredArgsConstructor
public class RejectedState implements SubscriptionRequestState {

    private final StreamBridge streamBridge;

    @Override
    public SubscriptionRequest handleRequest(SubscriptionRequest subscriptionRequest) {
        if(subscriptionRequest.getStatus().equals(Status.APPROVED)){
           throw new IllegalStateException("The subscription request has already been approved");
        }
        subscriptionRequest.setStatus(Status.REJECTED);
        streamBridge.send("sendRejectionMail-out-0",subscriptionRequest.getUniversityEmail());
        return subscriptionRequest;
    }
}
