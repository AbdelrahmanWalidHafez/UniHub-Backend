package com.unihub.chat.model.embedded;

import com.unihub.chat.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastMessage {

    private String messageId;
    private String contentPreview;
    private String senderEmail;
    private MessageType type;
    private LocalDateTime sentAt;
}