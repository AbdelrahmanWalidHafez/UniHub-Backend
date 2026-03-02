package com.unihub.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
