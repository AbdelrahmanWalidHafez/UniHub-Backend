package com.unihub.universitymanagement.universitymanagement.subscription.service;

import com.unihub.universitymanagement.universitymanagement.subscription.dto.request.SubscriptionPlan;

public interface ISubscriptionService {

     void setPlan(SubscriptionPlan request) ;

     void upgradePlan(SubscriptionPlan request);

}
