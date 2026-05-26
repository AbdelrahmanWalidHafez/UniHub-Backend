package com.unihub.chat.client.fallback;

import com.unihub.chat.client.AuthFeignClient;
import com.unihub.chat.client.dto.AuthUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AuthFallBack implements AuthFeignClient {

    @Override
    public AuthUserDto getUserInfo(String email, String apiKey) {
        log.error("Feign fallback: Auth service unavailable when fetching user info for {}", email);
        throw new IllegalStateException("Auth service is currently unavailable. Please try again later.");
    }

    @Override
    public List<AuthUserDto> searchUsers(String keyword, UUID tid, UUID cid, String apiKey) {
        log.error("Feign fallback: Auth service unavailable when searching users with keyword '{}'", keyword);
        return List.of();
    }
}
