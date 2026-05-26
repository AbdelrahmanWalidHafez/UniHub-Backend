package com.unihub.chat.ws.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingEvent {

    private String roomId;
    private String senderEmail;
    private boolean typing;
}
