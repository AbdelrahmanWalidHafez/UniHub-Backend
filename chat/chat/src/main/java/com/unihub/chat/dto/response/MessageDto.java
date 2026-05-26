package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.chat.model.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MessageDto {

    private String id;

    @JsonProperty("room_id")
    private String roomId;

    @JsonProperty("sender_email")
    private String senderEmail;

    @JsonProperty("sender_display_name")
    private String senderDisplayName;

    private MessageType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    @JsonProperty("image_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String imageUrl;

    @JsonProperty("voice_duration_secs")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer voiceDurationSecs;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MentionDto> mentions;

    @JsonProperty("reply_to")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReplyToDto replyTo;

    @JsonProperty("read_by")
    private List<String> readBy;

    @JsonProperty("edited_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime editedAt;

    @JsonProperty("deleted_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deletedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}