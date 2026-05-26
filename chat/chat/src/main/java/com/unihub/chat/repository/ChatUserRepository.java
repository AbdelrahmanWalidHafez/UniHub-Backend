package com.unihub.chat.repository;

import com.unihub.chat.model.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatUserRepository extends MongoRepository<ChatUser, String> {

    @Query("{ 'tid': ?0, 'cid': ?1, $or: [ { 'email': { $regex: ?2, $options: 'i' } }, { 'displayName': { $regex: ?2, $options: 'i' } } ] }")
    List<ChatUser> searchByCollegeAndQuery(UUID tid, UUID cid, String query);

    @Query("{ 'tid': ?0, $or: [ { 'email': { $regex: ?1, $options: 'i' } }, { 'displayName': { $regex: ?1, $options: 'i' } } ] }")
    List<ChatUser> searchByUniversityAndQuery(UUID tid, String query);
}