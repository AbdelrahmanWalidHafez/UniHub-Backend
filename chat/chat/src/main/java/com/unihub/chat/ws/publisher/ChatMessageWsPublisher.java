package com.unihub.chat.ws.publisher;

import com.unihub.chat.dto.response.MessageDto;
import com.unihub.chat.model.ChatRoom;
import com.unihub.chat.model.Message;
import com.unihub.chat.model.embedded.Participant;
import com.unihub.chat.repository.ChatRoomRepository;
import com.unihub.chat.ws.dto.RoomUpdateEvent;
import com.unihub.chat.ws.mapper.MessageWsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageWsPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageWsMapper messageWsMapper;

    public void publishMessage(Message message) {
        MessageDto dto = messageWsMapper.toDto(message);
        publishToRoom(message.getRoomId(), dto);
        publishRoomUpdateToParticipants(message.getRoomId(), dto);
    }

    private void publishToRoom(String roomId, MessageDto dto) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, dto);
    }

    private void publishRoomUpdateToParticipants(String roomId, MessageDto dto) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElse(null);
        if (room == null || room.getParticipants() == null) {
            return;
        }

        RoomUpdateEvent event = RoomUpdateEvent.builder()
                .type("MESSAGE")
                .roomId(roomId)
                .message(dto)
                .build();

        for (Participant participant : room.getParticipants()) {
            if (participant.getEmail() == null || participant.getEmail().isBlank()) {
                continue;
            }
            messagingTemplate.convertAndSendToUser(participant.getEmail(), "/queue/rooms", event);
        }
    }
}
