package com.unihub.universitymanagement.universitymanagement.internal.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SystemAdminRequest {

    private UUID tid;

    @JsonProperty("university_domain")
    private String universityDomain;
}
