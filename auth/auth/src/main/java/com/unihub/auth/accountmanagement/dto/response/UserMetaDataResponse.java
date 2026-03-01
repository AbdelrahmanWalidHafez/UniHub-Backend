package com.unihub.auth.accountmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMetaDataResponse {

    @JsonProperty("user_id")
    private UUID uid;

    private String email;

    @JsonProperty("college_id")
    private UUID cid;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
