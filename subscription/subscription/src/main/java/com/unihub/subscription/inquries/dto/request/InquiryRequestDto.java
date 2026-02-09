package com.unihub.subscription.inquries.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "subject cannot be blank")
    @Size(min = 3, max = 30, message = "Subject must be between 3 and 30 characters")
    private String subject;

    @NotBlank(message="Content cannot be blank")
    @Size(min = 5, max = 300, message = "Content must be between 5 and 300 characters")
    private String content;
}
