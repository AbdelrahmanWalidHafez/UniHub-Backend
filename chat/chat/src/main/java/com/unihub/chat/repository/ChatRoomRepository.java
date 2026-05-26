package com.unihub.chat.repository;

import com.unihub.chat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByTidAndParticipants_EmailOrderByUpdatedAtDesc(UUID tid, String email);

    @Query("{ 'type': 'DIRECT', 'participants.email': { $all: [?0, ?1] }, 'tid': ?2 }")
    Optional<ChatRoom> findDirectRoom(String emailA, String emailB, UUID tid);
}