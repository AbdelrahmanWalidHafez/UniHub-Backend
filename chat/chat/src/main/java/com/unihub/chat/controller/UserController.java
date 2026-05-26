package com.unihub.chat.controller;

import com.unihub.chat.common.util.HttpHeadersUtils;
import com.unihub.chat.dto.response.ChatUserDto;
import com.unihub.chat.model.ChatUser;
import com.unihub.chat.service.IChatUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final IChatUserService chatUserService;
    private final HttpHeadersUtils headersUtils;

    @GetMapping("/search")
    public ResponseEntity<List<ChatUserDto>> search(@RequestParam("q") String query,
                                                     HttpServletRequest httpRequest) {
        UUID tid = headersUtils.extractTid(httpRequest);
        UUID cid = headersUtils.extractCid(httpRequest);
        String requesterEmail = headersUtils.extractEmail(httpRequest);
        ChatUser requester = chatUserService.resolveUser(requesterEmail, tid, cid);

        List<ChatUserDto> results = chatUserService
                .searchUsers(query, tid, cid, requester.getRole())
                .stream()
                .filter(u -> !u.getEmail().equals(requesterEmail))
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(results);
    }

    @GetMapping("/{email}/presence")
    public ResponseEntity<ChatUserDto> getPresence(@PathVariable String email) {
        return ResponseEntity.ok(toDto(chatUserService.getPresence(email)));
    }

    private ChatUserDto toDto(ChatUser user) {
        return ChatUserDto.builder()
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .isOnline(user.isOnline())
                .lastSeenAt(user.getLastSeenAt())
                .build();
    }
}
