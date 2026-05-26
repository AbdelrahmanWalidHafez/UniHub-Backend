package com.unihub.chat.ws.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendMessageWsRequest {

    private String roomId;
    private String content;
    private String replyToMessageId;
    private List<String> mentionedEmails;
}
