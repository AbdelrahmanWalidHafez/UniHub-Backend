package com.unihub.subscription.subscriptionrequest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUniversityRequest {

    @JsonProperty("university_name")
    private String universityName;

    @JsonProperty("university_email")
    private String universityEmail;

    private String country;

    private String city;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("university_website_url")
    private String universityWebsiteUrl;

    @JsonProperty("university_domain")
    private String universityDomain;

    @JsonProperty("accreditation_key")
    private String accreditationKey;

    @JsonProperty("logo_key")
    private String logoKey;
}
