package com.unihub.subscription.inquries.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class InquiryRequestDto {

    @Email(message = "Must be a valid email")
    @NotBlank(message="Email cannot be blank")
    @JsonProperty("customer_email")
    private String customerEmail;

    @Min(value = 3,message = "content must be at least 3 characters")
    @Max(value = 300,message = "subject must be at most 30 characters")
    @NotBlank(message = "subject cannot be blank")
    private String subject;

    @NotBlank(message="Content cannot be blank")
    @Min(value = 5,message = "content must be at least 5 characters")
    @Max(value = 300,message = "content must be at most 300 characters")
    private String content;
}
