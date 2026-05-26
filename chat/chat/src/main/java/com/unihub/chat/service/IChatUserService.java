package com.unihub.chat.service;

import com.unihub.chat.model.ChatUser;

import java.util.List;
import java.util.UUID;

public interface IChatUserService {

    ChatUser resolveUser(String email, UUID tid, UUID cid);

    void markOnline(String email);

    void markOffline(String email);

    List<ChatUser> searchUsers(String query, UUID tid, UUID cid, String requesterRole);

    ChatUser getPresence(String email);
}