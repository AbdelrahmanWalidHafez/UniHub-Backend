package com.unihub.subscription.inquries.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponseDto {

    @JsonProperty("customer_email")
    private String customerEmail;

    private String subject;

    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
