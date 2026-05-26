package com.unihub.chat.controller;

import com.unihub.chat.common.util.HttpHeadersUtils;
import com.unihub.chat.dto.request.AddParticipantsRequest;
import com.unihub.chat.dto.request.CreateGroupRequest;
import com.unihub.chat.dto.request.StartDirectChatRequest;
import com.unihub.chat.dto.response.ChatRoomDto;
import com.unihub.chat.model.ChatRoom;
import com.unihub.chat.model.embedded.LastMessage;
import com.unihub.chat.model.embedded.Participant;
import com.unihub.chat.dto.response.LastMessageDto;
import com.unihub.chat.dto.response.ParticipantDto;
import com.unihub.chat.repository.ChatUserRepository;
import com.unihub.chat.service.IChatRoomService;
import com.unihub.chat.service.IMessageService;
import com.unihub.chat.ws.publisher.ChatMessageWsPublisher;
import com.unihub.chat.ws.publisher.ChatRoomWsPublisher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final IChatRoomService chatRoomService;
    private final IMessageService messageService;
    private final HttpHeadersUtils headersUtils;
    private final ChatRoomWsPublisher roomWsPublisher;
    private final ChatMessageWsPublisher messageWsPublisher;
    private final ChatUserRepository chatUserRepository;

    @PostMapping("/direct")
    public ResponseEntity<ChatRoomDto> startDirect(@RequestBody @Valid StartDirectChatRequest request,
                                                    HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        UUID tid = headersUtils.extractTid(httpRequest);
        ChatRoom room = chatRoomService.getOrCreateDirectRoom(email, request.getTargetEmail(), tid);
        return ResponseEntity.status(HttpStatus.OK).body(toDto(room, email));
    }

    @PostMapping("/group")
    public ResponseEntity<ChatRoomDto> createGroup(@RequestBody @Valid CreateGroupRequest request,
                                                    HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        UUID tid = headersUtils.extractTid(httpRequest);
        ChatRoom room = chatRoomService.createGroupRoom(email, request.getName(), request.getParticipantEmails(), tid);
        roomWsPublisher.publishRoomUpsert(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(room, email));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getMyRooms(HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        UUID tid = headersUtils.extractTid(httpRequest);
        List<ChatRoomDto> rooms = chatRoomService.getRoomsForUser(email, tid)
                .stream().map(room -> toDto(room, email)).toList();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDto> getRoom(@PathVariable String roomId, HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        return ResponseEntity.ok(toDto(chatRoomService.getRoomById(roomId, email), email));
    }

    @PatchMapping("/{roomId}/name")
    public ResponseEntity<ChatRoomDto> updateName(@PathVariable String roomId,
                                                   @RequestParam String name,
                                                   HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        return ResponseEntity.ok(toDto(chatRoomService.updateGroupName(roomId, name, email), email));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId, HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        ChatRoom room = chatRoomService.getRoomById(roomId, email);
        chatRoomService.deleteRoom(roomId, email);
        roomWsPublisher.publishRoomDeleted(room);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/participants")
    public ResponseEntity<Void> addParticipants(@PathVariable String roomId,
                                                 @RequestBody @Valid AddParticipantsRequest request,
                                                 HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        UUID tid = headersUtils.extractTid(httpRequest);
        UUID cid = headersUtils.extractCid(httpRequest);
        chatRoomService.addParticipants(roomId, request.getEmails(), email, tid, cid);
        ChatRoom updated = chatRoomService.getRoomById(roomId, email);
        roomWsPublisher.publishRoomUpsert(updated);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roomId}/participants/{targetEmail}")
    public ResponseEntity<Void> removeParticipant(@PathVariable String roomId,
                                                   @PathVariable String targetEmail,
                                                   HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        chatRoomService.removeParticipant(roomId, targetEmail, email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable String roomId, HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        String displayName = chatUserRepository.findById(email)
                .map(u -> u.getDisplayName())
                .orElse(email);
        ChatRoom updatedRoom = chatRoomService.leaveRoom(roomId, email);
        com.unihub.chat.model.Message systemMsg = messageService.sendSystemMessage(
                roomId, displayName + " left the group");
        messageWsPublisher.publishMessage(systemMsg);
        roomWsPublisher.publishRoomUpsert(updatedRoom);
        return ResponseEntity.noContent().build();
    }

    private ChatRoomDto toDto(ChatRoom room, String requesterEmail) {
        return ChatRoomDto.builder()
                .id(room.getId())
                .type(room.getType())
                .name(room.getName())
                .participants(room.getParticipants().stream().map(this::toParticipantDto).toList())
                .lastMessage(room.getLastMessage() != null ? toLastMessageDto(room.getLastMessage()) : null)
                .unreadCount(messageService.countUnread(room.getId(), requesterEmail))
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    private ParticipantDto toParticipantDto(Participant p) {
        return ParticipantDto.builder()
                .email(p.getEmail())
                .displayName(p.getDisplayName())
                .role(p.getRole())
                .lastSeenAt(p.getLastSeenAt())
                .build();
    }

    private LastMessageDto toLastMessageDto(LastMessage lm) {
        return LastMessageDto.builder()
                .messageId(lm.getMessageId())
                .contentPreview(lm.getContentPreview())
                .senderEmail(lm.getSenderEmail())
                .type(lm.getType())
                .sentAt(lm.getSentAt())
                .build();
    }
}
