package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.chat.model.enums.ChatType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomDto {

    private String id;

    private ChatType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    private List<ParticipantDto> participants;

    @JsonProperty("last_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LastMessageDto lastMessage;

    @JsonProperty("unread_count")
    private long unreadCount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}