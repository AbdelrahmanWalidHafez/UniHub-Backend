package com.unihub.subscription.subscriptionrequest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
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
    private String accreditation;

    @JsonProperty("system_admin")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    SystemAdminResponse systemAdmin;
}
