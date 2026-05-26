package com.unihub.chat.ws.mapper;

import com.unihub.chat.dto.response.MentionDto;
import com.unihub.chat.dto.response.MessageDto;
import com.unihub.chat.dto.response.ReplyToDto;
import com.unihub.chat.model.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageWsMapper {

    public MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .senderEmail(message.getSenderEmail())
                .senderDisplayName(message.getSenderDisplayName())
                .type(message.getType())
                .content(message.getContent())
                .imageUrl(message.getImageUrl())
                .voiceDurationSecs(message.getVoiceDurationSecs())
                .mentions(message.getMentions() == null ? List.of() :
                        message.getMentions().stream()
                                .map(m -> MentionDto.builder()
                                        .email(m.getEmail())
                                        .displayName(m.getDisplayName())
                                        .build())
                                .toList())
                .replyTo(message.getReplyTo() == null ? null :
                        ReplyToDto.builder()
                                .messageId(message.getReplyTo().getMessageId())
                                .senderDisplayName(message.getReplyTo().getSenderDisplayName())
                                .contentPreview(message.getReplyTo().getContentPreview())
                                .build())
                .readBy(message.getReadBy())
                .editedAt(message.getEditedAt())
                                .deletedAt(message.getDeletedAt())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
