package com.unihub.chat.service;

import com.unihub.chat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMessageService {

    Message sendTextMessage(String roomId, String senderEmail, String content,
                            String replyToMessageId, List<String> mentionedEmails);

    Message sendImageMessage(String roomId, String senderEmail, MultipartFile image,
                             String replyToMessageId, String content) throws IOException;

    long countUnread(String roomId, String email);

    Message sendVoiceMessage(String roomId, String senderEmail, MultipartFile voice,
                             Integer durationSecs, String replyToMessageId) throws IOException;

    Page<Message> getMessages(String roomId, String requesterEmail, int page);

    void markRead(String roomId, String readerEmail);

    Message editMessage(String messageId, String requesterEmail, String newContent);

    Message deleteMessage(String messageId, String requesterEmail);

    Message getVoiceMessage(String messageId, String requesterEmail);

    Message sendSystemMessage(String roomId, String content);
}
