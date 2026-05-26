package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.chat.model.enums.ParticipantRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipantDto {

    private String email;

    @JsonProperty("display_name")
    private String displayName;

    private ParticipantRole role;

    @JsonProperty("last_seen_at")
    private LocalDateTime lastSeenAt;
}