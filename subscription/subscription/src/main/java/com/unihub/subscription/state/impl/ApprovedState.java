package com.unihub.subscription.state.impl;

import com.unihub.subscription.subscriptionrequest.client.UniversityFeignClient;
import com.unihub.subscription.subscriptionrequest.dto.request.SendAcceptanceMailDto;
import com.unihub.subscription.subscriptionrequest.dto.response.UniversityResponse;
import com.unihub.subscription.subscriptionrequest.mapper.SubscriptionMapper;
import com.unihub.subscription.subscriptionrequest.model.Status;
import com.unihub.subscription.subscriptionrequest.model.SubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class ApprovedState implements SubscriptionRequestState {

    private final SubscriptionMapper subscriptionMapper;

    private final UniversityFeignClient universityFeignClient;

    private final StreamBridge streamBridge;

    @Override
    public SubscriptionRequest handleRequest(SubscriptionRequest subscriptionRequest) {
        if(subscriptionRequest.getStatus() == Status.APPROVED) {
            throw new IllegalArgumentException("Subscription request already approved");
        }
        UniversityResponse universityResponse = createUniversity(subscriptionRequest);
        if(universityResponse==null){
            throw new RuntimeException("Service might not be available right now or there maybe a university exists with the same data, subscription request will be pending until university is created.");
        }
        subscriptionRequest.setStatus(Status.APPROVED);
        streamBridge.send("sendAcceptanceMail-out-0",new SendAcceptanceMailDto(subscriptionRequest.getUniversityEmail(),universityResponse.getSystemAdmin()));
        return subscriptionRequest;
    }

    private UniversityResponse createUniversity(SubscriptionRequest subscriptionRequest){
        ResponseEntity<UniversityResponse> universityResponse=universityFeignClient.createUniversity(subscriptionMapper.toUniversityRequestDto(subscriptionRequest));
        return universityResponse.getBody();
    }

}
