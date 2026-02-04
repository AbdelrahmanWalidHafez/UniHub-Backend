package com.unihub.subscription.state.impl;

import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;

public class RejectedState implements SubscriptionRequestState {

    @Override
    public SubscriptionRequest handleRequest(SubscriptionRequest subscriptionRequest) {
        if(subscriptionRequest.getStatus().equals(Status.APPROVED)){
           throw new IllegalStateException("The subscription request has already been approved");
        }
        subscriptionRequest.setStatus(Status.REJECTED);
        //TODO send email
        return subscriptionRequest;
    }
}
