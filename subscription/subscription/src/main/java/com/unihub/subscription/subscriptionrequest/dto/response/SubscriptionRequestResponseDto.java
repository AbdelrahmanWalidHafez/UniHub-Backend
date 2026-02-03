package com.unihub.subscription.subscriptionrequest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.subscription.subscriptionrequest.model.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestResponseDto {

    @JsonProperty("request_id")
    private UUID rid;

    @JsonProperty("university_name")
    private String universityName;

    @JsonProperty("university_email")
    private String universityEmail;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("website_url")
    private String universityWebsiteUrl;

    @JsonProperty("university_domain")
    private String universityDomain;

    @JsonProperty("logo_key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityLogo;

    @JsonProperty("accreditation_key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accreditationKey;

    @JsonProperty("subscription_request_status")
    private Status status;

    @JsonProperty("created_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;


    @JsonProperty("updated_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedBy;
}
