package com.unihub.subscription.subscriptionrequest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionRequestDto {

    @Size(min = 3, max = 150, message = "University name must be between 3 and 150 characters")
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "University name contains invalid characters"
    )
    @JsonProperty("university_name")
    private String universityName;

    @NotBlank(message = "University email cannot be empty")
    @Email(message = "Invalid email address")
    @JsonProperty("university_email")
    private String universityEmail;

    @NotBlank(message = "Country cannot be empty")
    @Size(min = 2, max = 56, message = "Invalid country name")
    private String country;

    @NotBlank(message = "City cannot be empty")
    @Size(min = 2, max = 100, message = "City name is too short or too long")
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "City name contains invalid characters")
    private String city;

    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Invalid phone number"
    )
    @NotBlank(message = "Number cannot be empty")
    @JsonProperty("contact_number")
    private String contactNumber;

    @URL(message = "Invalid url")
    @NotBlank(message = "University website url cannot be empty")
    @JsonProperty("university_website_url")
    private String universityWebsiteUrl;

    @Pattern(
            regexp = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid university domain"
    )
    @NotBlank(message = "University domain cannot be empty")
    @JsonProperty("university_domain")
    private String universityDomain;

}
