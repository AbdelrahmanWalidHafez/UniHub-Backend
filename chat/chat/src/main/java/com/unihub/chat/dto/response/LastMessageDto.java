package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.chat.model.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LastMessageDto {

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("content_preview")
    private String contentPreview;

    @JsonProperty("sender_email")
    private String senderEmail;

    private MessageType type;

    @JsonProperty("sent_at")
    private LocalDateTime sentAt;
}