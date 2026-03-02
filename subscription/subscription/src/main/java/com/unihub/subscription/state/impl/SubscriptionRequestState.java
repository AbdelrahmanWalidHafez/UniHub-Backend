package com.unihub.subscription.state.impl;

import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;

public interface SubscriptionRequestState {

    SubscriptionRequest handleRequest(SubscriptionRequest subscriptionRequest);
}
