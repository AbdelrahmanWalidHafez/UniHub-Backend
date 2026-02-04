package com.unihub.subscription.state;

import com.unihub.subscription.state.impl.SubscriptionRequestState;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class SubscriptionRequestContext {

    private SubscriptionRequestState subscriptionRequestState;

    public SubscriptionRequest request(SubscriptionRequest subscriptionRequest){
        return subscriptionRequestState.handleRequest(subscriptionRequest);
    }
}
