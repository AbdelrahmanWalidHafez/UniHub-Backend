package com.unihub.subscription.subscriptionrequest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SubscriptionRequestResponses {
    @JsonProperty("subscription-requests")
    List<SubscriptionsMetaData> metaDataList;
}
