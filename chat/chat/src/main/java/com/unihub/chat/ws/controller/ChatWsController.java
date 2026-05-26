package com.unihub.chat.ws.controller;

import com.unihub.chat.dto.response.MessageDto;
import com.unihub.chat.model.Message;
import com.unihub.chat.service.IMessageService;
import com.unihub.chat.ws.dto.ReadReceiptEvent;
import com.unihub.chat.ws.dto.SendMessageWsRequest;
import com.unihub.chat.ws.dto.TypingEvent;
import com.unihub.chat.ws.publisher.ChatMessageWsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final IMessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageWsPublisher messageWsPublisher;

    @MessageMapping("chat.send")
    public void sendMessage(@Payload SendMessageWsRequest request, Principal principal) {
        String senderEmail = principal.getName();
        Message message = messageService.sendTextMessage(
                request.getRoomId(),
                senderEmail,
                request.getContent(),
                request.getReplyToMessageId(),
                request.getMentionedEmails()
        );
        messageWsPublisher.publishMessage(message);
    }

    @MessageMapping("chat.typing")
    public void typing(@Payload TypingEvent event, Principal principal) {
        event.setSenderEmail(principal.getName());
        messagingTemplate.convertAndSend("/topic/room/" + event.getRoomId() + "/typing", event);
    }

    @MessageMapping("chat.read")
    public void markRead(@Payload ReadReceiptEvent event, Principal principal) {
        String readerEmail = principal.getName();
        messageService.markRead(event.getRoomId(), readerEmail);
        event.setReaderEmail(readerEmail);
        messagingTemplate.convertAndSend("/topic/room/" + event.getRoomId() + "/read", event);
    }
}
