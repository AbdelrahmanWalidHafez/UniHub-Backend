package com.unihub.chat.service;

import com.unihub.chat.model.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface IChatRoomService {

    ChatRoom getOrCreateDirectRoom(String senderEmail, String targetEmail, UUID tid);

    ChatRoom createGroupRoom(String creatorEmail, String name, List<String> participantEmails, UUID tid);

    List<ChatRoom> getRoomsForUser(String email, UUID tid);

    ChatRoom getRoomById(String roomId, String requesterEmail);

    ChatRoom updateGroupName(String roomId, String name, String requesterEmail);

    void addParticipants(String roomId, List<String> emails, String requesterEmail, UUID tid, UUID cid);

    void removeParticipant(String roomId, String targetEmail, String requesterEmail);

    ChatRoom leaveRoom(String roomId, String requesterEmail);

    void deleteRoom(String roomId, String requesterEmail);
}
