package com.unihub.subscription.subscriptionrequest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.subscription.subscriptionrequest.dto.response.SystemAdminResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendAcceptanceMailDto {

    String to;

    @JsonProperty("system_admin_response")
    SystemAdminResponse systemAdminResponse;
}
