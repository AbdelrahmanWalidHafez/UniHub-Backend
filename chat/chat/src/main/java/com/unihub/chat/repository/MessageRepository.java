package com.unihub.chat.repository;

import com.unihub.chat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    Page<Message> findByRoomIdAndDeletedAtIsNullOrderByCreatedAtDesc(String roomId, Pageable pageable);

    Page<Message> findByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);

    List<Message> findByRoomIdAndDeletedAtIsNullAndReadByNotContaining(String roomId, String email);

    long countByRoomIdAndDeletedAtIsNullAndReadByNotContaining(String roomId, String email);

    void deleteByRoomId(String roomId);
}