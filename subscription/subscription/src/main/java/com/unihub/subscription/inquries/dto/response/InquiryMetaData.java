package com.unihub.subscription.inquries.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryMetaData {

    private UUID id;

    @JsonProperty("customer_email")
    private String customerEmail;

    private String subject;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
