package com.unihub.chat.client;

import com.unihub.chat.client.dto.AuthUserDto;
import com.unihub.chat.client.fallback.AuthFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "auth", fallback = AuthFallBack.class)
public interface AuthFeignClient {

    @GetMapping("/api/v1/internal/user/user-info")
    AuthUserDto getUserInfo(@RequestHeader("X-User-Email") String email,
                            @RequestHeader("X-API-KEY") String apiKey);

    @GetMapping("/api/v1/internal/user/search")
    List<AuthUserDto> searchUsers(@RequestParam("q") String keyword,
                                  @RequestParam("tid") UUID tid,
                                  @RequestParam(value = "cid", required = false) UUID cid,
                                  @RequestHeader("X-API-KEY") String apiKey);
}
