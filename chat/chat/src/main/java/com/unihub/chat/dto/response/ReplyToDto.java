package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyToDto {

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("sender_display_name")
    private String senderDisplayName;

    @JsonProperty("content_preview")
    private String contentPreview;
}