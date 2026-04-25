package com.unihub.subscription.subscriptionrequest.dto.response;

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
public class SubscriptionsMetaData {

    @JsonProperty("request_id")
    private UUID rid;

    @JsonProperty("university_name")
    private String universityName;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("status")
    private Status status;
}
