package com.unihub.subscription.inquries.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryMetaData {

    private String customerEmail;

    private String subject;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
