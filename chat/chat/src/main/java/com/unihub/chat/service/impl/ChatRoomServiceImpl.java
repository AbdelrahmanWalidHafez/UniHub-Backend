package com.unihub.chat.service.impl;

import com.unihub.chat.model.ChatRoom;
import com.unihub.chat.model.ChatUser;
import com.unihub.chat.model.embedded.Participant;
import com.unihub.chat.model.enums.ChatType;
import com.unihub.chat.model.enums.ParticipantRole;
import com.unihub.chat.repository.ChatRoomRepository;
import com.unihub.chat.repository.MessageRepository;
import com.unihub.chat.service.IChatRoomService;
import com.unihub.chat.service.IChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements IChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final IChatUserService chatUserService;

    @Override
    public ChatRoom getOrCreateDirectRoom(String senderEmail, String targetEmail, UUID tid) {
        return chatRoomRepository.findDirectRoom(senderEmail, targetEmail, tid)
                .orElseGet(() -> {
                    ChatUser sender = chatUserService.resolveUser(senderEmail, tid, null);
                    ChatUser target = chatUserService.resolveUser(targetEmail, tid, null);

                    List<Participant> participants = List.of(
                            buildParticipant(sender, ParticipantRole.MEMBER),
                            buildParticipant(target, ParticipantRole.MEMBER)
                    );

                    ChatRoom room = ChatRoom.builder()
                            .type(ChatType.DIRECT)
                            .tid(tid)
                            .participants(participants)
                            .createdBy(senderEmail)
                            .build();

                    return chatRoomRepository.save(room);
                });
    }

    @Override
    public ChatRoom createGroupRoom(String creatorEmail, String name, List<String> participantEmails, UUID tid) {
        ChatUser creator = chatUserService.resolveUser(creatorEmail, tid, null);

        List<Participant> participants = new ArrayList<>();
        participants.add(buildParticipant(creator, ParticipantRole.ADMIN));

        for (String email : participantEmails) {
            if (!email.equals(creatorEmail)) {
                ChatUser member = chatUserService.resolveUser(email, tid, null);
                participants.add(buildParticipant(member, ParticipantRole.MEMBER));
            }
        }

        ChatRoom room = ChatRoom.builder()
                .type(ChatType.GROUP)
                .name(name)
                .tid(tid)
                .participants(participants)
                .createdBy(creatorEmail)
                .build();

        return chatRoomRepository.save(room);
    }

    @Override
    public List<ChatRoom> getRoomsForUser(String email, UUID tid) {
        return chatRoomRepository.findByTidAndParticipants_EmailOrderByUpdatedAtDesc(tid, email);
    }

    @Override
    public ChatRoom getRoomById(String roomId, String requesterEmail) {
        ChatRoom room = fetchRoom(roomId);
        assertParticipant(room, requesterEmail);
        return room;
    }

    @Override
    public ChatRoom updateGroupName(String roomId, String name, String requesterEmail) {
        ChatRoom room = fetchRoom(roomId);
        assertAdmin(room, requesterEmail);
        room.setName(name);
        return chatRoomRepository.save(room);
    }

    @Override
    public void addParticipants(String roomId, List<String> emails, String requesterEmail, UUID tid, UUID cid) {
        ChatRoom room = fetchRoom(roomId);
        assertAdmin(room, requesterEmail);

        for (String email : emails) {
            boolean alreadyIn = room.getParticipants().stream()
                    .anyMatch(p -> p.getEmail().equals(email));
            if (!alreadyIn) {
                ChatUser user = chatUserService.resolveUser(email, tid, cid);
                room.getParticipants().add(buildParticipant(user, ParticipantRole.MEMBER));
            }
        }
        chatRoomRepository.save(room);
    }

    @Override
    public void removeParticipant(String roomId, String targetEmail, String requesterEmail) {
        ChatRoom room = fetchRoom(roomId);
        assertAdmin(room, requesterEmail);
        room.getParticipants().removeIf(p -> p.getEmail().equals(targetEmail));
        chatRoomRepository.save(room);
    }

    @Override
    public ChatRoom leaveRoom(String roomId, String requesterEmail) {
        ChatRoom room = fetchRoom(roomId);
        if (room.getType() != ChatType.GROUP) {
            throw new IllegalArgumentException("You can only leave group rooms");
        }
        assertParticipant(room, requesterEmail);
        room.getParticipants().removeIf(p -> p.getEmail().equals(requesterEmail));
        return chatRoomRepository.save(room);
    }

    @Override
    public void deleteRoom(String roomId, String requesterEmail) {
        ChatRoom room = fetchRoom(roomId);
        if (room.getType() != ChatType.GROUP) {
            throw new IllegalArgumentException("Only group rooms can be deleted");
        }
        assertAdmin(room, requesterEmail);
        messageRepository.deleteByRoomId(roomId);
        chatRoomRepository.deleteById(roomId);
    }

    private ChatRoom fetchRoom(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
    }

    private void assertParticipant(ChatRoom room, String email) {
        boolean isMember = room.getParticipants().stream()
                .anyMatch(p -> p.getEmail().equals(email));
        if (!isMember) {
            throw new IllegalStateException("Access denied to room: " + room.getId());
        }
    }

    private void assertAdmin(ChatRoom room, String email) {
        boolean isAdmin = room.getParticipants().stream()
                .anyMatch(p -> p.getEmail().equals(email) && ParticipantRole.ADMIN.equals(p.getRole()));
        if (!isAdmin) {
            throw new IllegalStateException("Only group admins can perform this action");
        }
    }

    private Participant buildParticipant(ChatUser user, ParticipantRole role) {
        return Participant.builder()
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role(role)
                .lastSeenAt(LocalDateTime.now())
                .build();
    }
}
