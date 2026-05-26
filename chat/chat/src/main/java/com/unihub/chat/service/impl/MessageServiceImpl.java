package com.unihub.chat.service.impl;

import com.unihub.chat.client.S3FeignClient;
import com.unihub.chat.client.dto.UploadFileRequest;
import com.unihub.chat.model.ChatRoom;
import com.unihub.chat.model.ChatUser;
import com.unihub.chat.model.Message;
import com.unihub.chat.model.embedded.LastMessage;
import com.unihub.chat.model.embedded.Mention;
import com.unihub.chat.model.embedded.ReplyTo;
import com.unihub.chat.model.enums.MessageType;
import com.unihub.chat.repository.ChatRoomRepository;
import com.unihub.chat.repository.ChatUserRepository;
import com.unihub.chat.repository.MessageRepository;
import com.unihub.chat.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private static final int PAGE_SIZE = 30;

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final S3FeignClient s3FeignClient;

    @Value("${aws.bucket}")
    private String bucketLink;

    @Override
    public Message sendTextMessage(String roomId, String senderEmail, String content,
                                   String replyToMessageId, List<String> mentionedEmails) {
        ChatRoom room = fetchRoomAndAssertMembership(roomId, senderEmail);
        String displayName = resolveDisplayName(senderEmail);

        List<Mention> mentions = buildMentions(mentionedEmails);
        ReplyTo replyTo = buildReplyTo(replyToMessageId);

        Message message = Message.builder()
                .roomId(roomId)
                .senderEmail(senderEmail)
                .senderDisplayName(displayName)
                .type(MessageType.TEXT)
                .content(content)
                .mentions(mentions)
                .replyTo(replyTo)
                .readBy(new ArrayList<>(List.of(senderEmail)))
                .build();

        Message saved = messageRepository.save(message);
        updateLastMessage(room, saved, content);
        return saved;
    }

    @Override
    public Message sendImageMessage(String roomId, String senderEmail, MultipartFile image,
                                    String replyToMessageId, String content) throws IOException {
        ChatRoom room = fetchRoomAndAssertMembership(roomId, senderEmail);
        String displayName = resolveDisplayName(senderEmail);

        String originalName = image.getOriginalFilename() == null
            ? "attachment"
            : image.getOriginalFilename().replaceAll("\\s+", "_");
        String key = "chat/" + roomId + "/" + UUID.randomUUID() + "_" + originalName;
        s3FeignClient.uploadFile(UploadFileRequest.builder()
                .fileContent(image.getBytes())
                .key(key)
                .contentType(image.getContentType())
                .build());

        String imageUrl = buildBucketObjectUrl(key);
        ReplyTo replyTo = buildReplyTo(replyToMessageId);

        Message message = Message.builder()
                .roomId(roomId)
                .senderEmail(senderEmail)
                .senderDisplayName(displayName)
                .type(MessageType.IMAGE)
                .imageUrl(imageUrl)
                .content(content)
                .replyTo(replyTo)
                .readBy(new ArrayList<>(List.of(senderEmail)))
                .build();

        String preview = (content != null && !content.isBlank()) ? " " + content : " Image";
        Message saved = messageRepository.save(message);
        updateLastMessage(room, saved, preview);
        return saved;
    }

    @Override
    public Message sendVoiceMessage(String roomId, String senderEmail, MultipartFile voice,
                                    Integer durationSecs, String replyToMessageId) throws IOException {
        ChatRoom room = fetchRoomAndAssertMembership(roomId, senderEmail);
        String displayName = resolveDisplayName(senderEmail);
        ReplyTo replyTo = buildReplyTo(replyToMessageId);

        Message message = Message.builder()
                .roomId(roomId)
                .senderEmail(senderEmail)
                .senderDisplayName(displayName)
                .type(MessageType.VOICE)
                .voiceData(voice.getBytes())
                .voiceDurationSecs(durationSecs)
                .replyTo(replyTo)
                .readBy(new ArrayList<>(List.of(senderEmail)))
                .build();

        Message saved = messageRepository.save(message);
        updateLastMessage(room, saved, "Voice message");
        return saved;
    }

    @Override
    public Page<Message> getMessages(String roomId, String requesterEmail, int page) {
        fetchRoomAndAssertMembership(roomId, requesterEmail);
        return messageRepository.findByRoomIdOrderByCreatedAtDesc(
                roomId, PageRequest.of(page - 1, PAGE_SIZE));
    }

    @Override
    public void markRead(String roomId, String readerEmail) {
        fetchRoomAndAssertMembership(roomId, readerEmail);
        List<Message> unread = messageRepository
                .findByRoomIdAndDeletedAtIsNullAndReadByNotContaining(roomId, readerEmail);
        unread.forEach(m -> m.getReadBy().add(readerEmail));
        messageRepository.saveAll(unread);
    }

    @Override
    public long countUnread(String roomId, String email) {
        return messageRepository.countByRoomIdAndDeletedAtIsNullAndReadByNotContaining(roomId, email);
    }

    @Override
    public Message editMessage(String messageId, String requesterEmail, String newContent) {
        Message message = fetchMessageAndAssertOwner(messageId, requesterEmail);
        if (message.getType() != MessageType.TEXT) {
            throw new IllegalArgumentException("Only text messages can be edited");
        }
        message.setContent(newContent);
        message.setEditedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Message deleteMessage(String messageId, String requesterEmail) {
        Message message = fetchMessageAndAssertOwner(messageId, requesterEmail);
        message.setDeletedAt(LocalDateTime.now());
        message.setContent(null);
        message.setImageUrl(null);
        message.setVoiceData(null);
        message.setMentions(new ArrayList<>());
        message.setReplyTo(null);
        return messageRepository.save(message);
    }

    @Override
    public Message getVoiceMessage(String messageId, String requesterEmail) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        if (message.getType() != MessageType.VOICE) {
            throw new IllegalArgumentException("Message is not a voice message");
        }
        if (message.getDeletedAt() != null) {
            throw new IllegalArgumentException("Message has been deleted");
        }
        fetchRoomAndAssertMembership(message.getRoomId(), requesterEmail);
        return message;
    }

    @Override
    public Message sendSystemMessage(String roomId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        Message message = Message.builder()
                .roomId(roomId)
                .type(MessageType.SYSTEM)
                .content(content)
                .readBy(new ArrayList<>())
                .build();
        Message saved = messageRepository.save(message);
        updateLastMessage(room, saved, content);
        return saved;
    }

    private ChatRoom fetchRoomAndAssertMembership(String roomId, String email) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        boolean isMember = room.getParticipants().stream()
                .anyMatch(p -> p.getEmail().equals(email));
        if (!isMember) {
            throw new IllegalStateException("Access denied to room: " + roomId);
        }
        return room;
    }

    private Message fetchMessageAndAssertOwner(String messageId, String email) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        if (!message.getSenderEmail().equals(email)) {
            throw new IllegalStateException("You can only modify your own messages");
        }
        if (message.getDeletedAt() != null) {
            throw new IllegalArgumentException("Message already deleted");
        }
        return message;
    }

    private String resolveDisplayName(String email) {
        return chatUserRepository.findById(email)
                .map(ChatUser::getDisplayName)
                .orElse(email);
    }

    private List<Mention> buildMentions(List<String> emails) {
        if (emails == null || emails.isEmpty()) return new ArrayList<>();
        return emails.stream()
                .map(email -> {
                    String displayName = chatUserRepository.findById(email)
                            .map(u -> u.getDisplayName())
                            .orElse(email);
                    return Mention.builder().email(email).displayName(displayName).build();
                })
                .toList();
    }

    private ReplyTo buildReplyTo(String replyToMessageId) {
        if (replyToMessageId == null) return null;
        return messageRepository.findById(replyToMessageId)
                .map(m -> ReplyTo.builder()
                        .messageId(m.getId())
                        .senderDisplayName(m.getSenderDisplayName())
                        .contentPreview(m.getType() == MessageType.TEXT
                                ? truncate(m.getContent(), 80)
                                : m.getType().name())
                        .build())
                .orElse(null);
    }

    private void updateLastMessage(ChatRoom room, Message message, String preview) {
        room.setLastMessage(LastMessage.builder()
                .messageId(message.getId())
                .contentPreview(preview)
                .senderEmail(message.getSenderEmail())
                .type(message.getType())
                .sentAt(message.getCreatedAt())
                .build());
        chatRoomRepository.save(room);
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max) + "…";
    }

    private String buildBucketObjectUrl(String key) {
        if (bucketLink == null || bucketLink.isBlank()) {
            return key;
        }
        String base = bucketLink.trim();
        return base.endsWith("/") ? base + key : base + "/" + key;
    }
}
