package com.unihub.auth.internal.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {

    @JsonProperty("university_id")
    private UUID uniId;

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
    private String universityLogo;

    @JsonProperty("accreditation_key")
    private String accreditationKey;

    @JsonProperty("subscription_plan")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UniversitySubscriptionPlanResponse subscriptionPlan;

    @JsonProperty("system_admin")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    SystemAdminResponse systemAdmin;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;
}
