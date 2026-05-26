package com.unihub.chat.ws.publisher;

import com.unihub.chat.model.ChatRoom;
import com.unihub.chat.model.embedded.Participant;
import com.unihub.chat.ws.dto.RoomUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomWsPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishRoomUpsert(ChatRoom room) {
        if (room == null || room.getId() == null || room.getParticipants() == null) {
            return;
        }

        RoomUpdateEvent event = RoomUpdateEvent.builder()
                .type("ROOM_UPSERT")
                .roomId(room.getId())
                .build();

        for (Participant participant : room.getParticipants()) {
            if (participant.getEmail() == null || participant.getEmail().isBlank()) {
                continue;
            }
            messagingTemplate.convertAndSendToUser(participant.getEmail(), "/queue/rooms", event);
        }
    }

    public void publishRoomDeleted(ChatRoom room) {
        if (room == null || room.getId() == null || room.getParticipants() == null) {
            return;
        }

        RoomUpdateEvent event = RoomUpdateEvent.builder()
                .type("ROOM_DELETED")
                .roomId(room.getId())
                .build();

        for (Participant participant : room.getParticipants()) {
            if (participant.getEmail() == null || participant.getEmail().isBlank()) {
                continue;
            }
            messagingTemplate.convertAndSendToUser(participant.getEmail(), "/queue/rooms", event);
        }
    }
}