package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatUserDto {

    private String email;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("is_online")
    private boolean isOnline;

    @JsonProperty("last_seen_at")
    private LocalDateTime lastSeenAt;
}