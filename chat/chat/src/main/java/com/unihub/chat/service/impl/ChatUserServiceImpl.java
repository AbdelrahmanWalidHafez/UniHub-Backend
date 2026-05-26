package com.unihub.chat.service.impl;

import com.unihub.chat.client.AuthFeignClient;
import com.unihub.chat.client.dto.AuthUserDto;
import com.unihub.chat.model.ChatUser;
import com.unihub.chat.repository.ChatUserRepository;
import com.unihub.chat.service.IChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements IChatUserService {

    private static final String SYSTEM_ADMIN_ROLE = "ROLE_SYSTEM_ADMIN";

    private final ChatUserRepository chatUserRepository;
    private final AuthFeignClient authFeignClient;

    @Value("${api.key}")
    private String apiKey;

    @Override
    public ChatUser resolveUser(String email, UUID tid, UUID cid) {
        return chatUserRepository.findById(email).orElseGet(() -> {
            AuthUserDto authUser = authFeignClient.getUserInfo(email, apiKey);
            ChatUser user = ChatUser.builder()
                    .email(email)
                    .displayName(authUser.getFirstName() + " " + authUser.getLastName())
                    .tid(authUser.getUniversityMetadata() != null ? authUser.getUniversityMetadata().getTid() : tid)
                    .cid(authUser.getUniversityMetadata() != null ? authUser.getUniversityMetadata().getCid() : cid)
                    .role(authUser.getRole() != null ? authUser.getRole().getName() : "")
                    .isOnline(false)
                    .lastSeenAt(LocalDateTime.now())
                    .build();
            return chatUserRepository.save(user);
        });
    }

    @Override
    public void markOnline(String email) {
        chatUserRepository.findById(email).ifPresent(user -> {
            user.setOnline(true);
            user.setLastSeenAt(LocalDateTime.now());
            chatUserRepository.save(user);
        });
    }

    @Override
    public void markOffline(String email) {
        chatUserRepository.findById(email).ifPresent(user -> {
            user.setOnline(false);
            user.setLastSeenAt(LocalDateTime.now());
            chatUserRepository.save(user);
        });
    }

    @Override
    public List<ChatUser> searchUsers(String query, UUID tid, UUID cid, String requesterRole) {
        UUID cidFilter = SYSTEM_ADMIN_ROLE.equals(requesterRole) ? null : cid;
        return authFeignClient.searchUsers(query, tid, cidFilter, apiKey)
                .stream()
                .map(authUser -> ChatUser.builder()
                        .email(authUser.getEmail())
                        .displayName(authUser.getFirstName() + " " + authUser.getLastName())
                        .tid(authUser.getUniversityMetadata() != null ? authUser.getUniversityMetadata().getTid() : tid)
                        .cid(authUser.getUniversityMetadata() != null ? authUser.getUniversityMetadata().getCid() : cid)
                        .role(authUser.getRole() != null ? authUser.getRole().getName() : "")
                        .build())
                .toList();
    }

    @Override
    public ChatUser getPresence(String email) {
        return chatUserRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}